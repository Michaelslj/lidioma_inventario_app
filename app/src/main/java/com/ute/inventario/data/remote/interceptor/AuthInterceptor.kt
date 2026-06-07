package com.ute.inventario.data.remote.interceptor

import com.google.gson.Gson
import com.ute.inventario.BuildConfig
import com.ute.inventario.data.local.TokenDataStore
import com.ute.inventario.data.remote.dto.TokenRefreshRequestDto
import com.ute.inventario.data.remote.dto.TokenRefreshResponseDto
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAuthenticator @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Si ya intentamos reintentar una vez, no lo hacemos de nuevo para evitar bucles
        if (response.request.header("X-Retry") != null) return null

        val refreshToken = runBlocking { tokenDataStore.getRefreshToken() } ?: return null

        val client = OkHttpClient()
        val gson = Gson()
        val body = gson.toJson(TokenRefreshRequestDto(refreshToken))

        val request = Request.Builder()
            .url("${BuildConfig.API_BASE_URL}auth/token/refresh/")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        val refreshResponse = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            return null
        }

        if (!refreshResponse.isSuccessful) {
            // Si el refresh falla, ahí sí borramos la sesión
            runBlocking { tokenDataStore.clearSession() }
            return null
        }

        val responseBody = refreshResponse.body?.string() ?: return null
        val refreshDto = try {
            gson.fromJson(responseBody, TokenRefreshResponseDto::class.java)
        } catch (e: Exception) { null }

        val newAccess = refreshDto?.access ?: return null

        runBlocking { 
            tokenDataStore.saveTokens(newAccess, refreshDto.refresh)
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .header("X-Retry", "true")
            .build()
    }
}

class BearerTokenInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenDataStore.getAccessToken() }
        val request = chain.request().newBuilder().apply {
            if (accessToken != null) {
                header("Authorization", "Bearer $accessToken")
            }
        }.build()

        return chain.proceed(request)
    }
}
