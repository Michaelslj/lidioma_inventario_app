package com.ute.inventario.di

import com.ute.inventario.data.repository.*
import com.ute.inventario.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindCategoriaRepository(impl: CategoriaRepositoryImpl): CategoriaRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(impl: ProductoRepositoryImpl): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindMovimientoInventarioRepository(impl: MovimientoInventarioRepositoryImpl): MovimientoInventarioRepository

    @Binds
    @Singleton
    abstract fun bindOrdenCompraRepository(impl: OrdenCompraRepositoryImpl): OrdenCompraRepository

    @Binds
    @Singleton
    abstract fun bindProveedorRepository(impl: ProveedorRepositoryImpl): ProveedorRepository
}