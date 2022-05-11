package com.sliide.technicaltaskandroid.daggerhilt

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.sliide.technicaltaskandroid.data.errors.DataErrorHandler
import com.sliide.technicaltaskandroid.data.user.UserExtractor
import com.sliide.technicaltaskandroid.data.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRequestQueue(
        @ApplicationContext context: Context
    ): RequestQueue = Volley.newRequestQueue(context)

    @Singleton
    @Provides
    fun providesUserExtractor(): UserExtractor = UserExtractor()

    @Singleton
    @Provides
    fun providesDataErrorHandler(
        @ApplicationContext context: Context
    ): DataErrorHandler = DataErrorHandler(context)

    @Singleton
    @Provides
    fun providesUserRepository(
        requestQueue: RequestQueue,
        userExtractor: UserExtractor,
        dataErrorHandler: DataErrorHandler
    ): UserRepository = UserRepository(requestQueue, userExtractor, dataErrorHandler)
}