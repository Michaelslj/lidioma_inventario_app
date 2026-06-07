package com.ute.inventario.di

import com.google.gson.GsonBuilder
import com.ute.inventario.BuildConfig
import com.ute.inventario.data.local.TokenDataStore
import com.ute.inventario.data.remote.api.*
import com.ute.inventario.data.remote.interceptor.AuthAuthenticator
import com.ute.inventario.data.remote.interceptor.BearerTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenDataStore: TokenDataStore,
        authAuthenticator: AuthAuthenticator,
        logging: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .authenticator(authAuthenticator) // Renueva el token automáticamente en 401
        .addInterceptor(BearerTokenInterceptor(tokenDataStore)) // Añade Bearer a cada request
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    // ── Provisión de APIs ──────────────────────────────────────────

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCategoriaApi(retrofit: Retrofit): CategoriaApi = retrofit.create(CategoriaApi::class.java)

    @Provides
    @Singleton
    fun provideProductoApi(retrofit: Retrofit): ProductoApi = retrofit.create(ProductoApi::class.java)

    @Provides
    @Singleton
    fun provideMovimientoInventarioApi(retrofit: Retrofit): MovimientoInventarioApi =
        retrofit.create(MovimientoInventarioApi::class.java)

    @Provides
    @Singleton
    fun provideProveedorApi(retrofit: Retrofit): ProveedorApi = retrofit.create(ProveedorApi::class.java)

    @Provides
    @Singleton
    fun provideOrdenCompraApi(retrofit: Retrofit): OrdenCompraApi = retrofit.create(OrdenCompraApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}