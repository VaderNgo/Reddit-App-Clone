package com.example.baddit.di

import com.example.baddit.data.repository.AuthRepositoryImpl
import com.example.baddit.data.repository.CommentRepositoryImpl
import com.example.baddit.data.repository.CommunityRepositoryImpl
import com.example.baddit.data.repository.PostRepositoryImpl
import com.example.baddit.data.repository.UserRepositoryImpl
import com.example.baddit.domain.repository.AuthRepository
import com.example.baddit.domain.repository.CommentRepository
import com.example.baddit.domain.repository.CommunityRepository
import com.example.baddit.domain.repository.PostRepository
import com.example.baddit.domain.repository.UserRepository
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
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCommunityRepository(impl: CommunityRepositoryImpl): CommunityRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl):UserRepository

}