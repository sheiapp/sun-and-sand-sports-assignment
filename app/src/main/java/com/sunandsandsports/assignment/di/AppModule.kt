package com.sunandsandsports.assignment.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sunandsandsports.assignment.R
import com.sunandsandsports.assignment.data.data_source.RandomUserPagingDataSource
import com.sunandsandsports.assignment.data.repository.RandomUserRepository
import com.sunandsandsports.assignment.data.repository.RandomUserRepositoryImpl
import com.sunandsandsports.assignment.api.RandomUserApi
import com.sunandsandsports.assignment.api.RandomUserApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 20/04/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun loggingInterceptorProvider(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun interceptorProvider(): Interceptor = Interceptor {
        val originalRequest = it.request()
        val builder =
            originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
        val newRequest = builder.build()
        it.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun converterFactoryProvider(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun okHttpClientProvider(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Provides
    @Singleton
    fun retrofitProvider(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        addConverterFactory(converterFactory)
        client(okHttpClient)
    }.build()

    @Provides
    @Singleton
    fun randomUserApiProvider(retrofit: Retrofit): RandomUserApi =
        retrofit.create(RandomUserApi::class.java)


    @Provides
    @Singleton
    fun initGlide(@ApplicationContext appContext: Context): RequestManager = Glide.with(appContext)
        .setDefaultRequestOptions(
            RequestOptions()
                .centerInside()
                .error(R.drawable.ic_user)
        )

    @Provides
    @Singleton
    fun randomUserPagingDataSourceProvider(randomUserApi: RandomUserApi): RandomUserPagingDataSource =
        RandomUserPagingDataSource(randomUserApi)

    @Provides
    @Singleton
    fun randomUserRepositoryProvider(randomUserPagingDataSource: RandomUserPagingDataSource): RandomUserRepository =
        RandomUserRepositoryImpl(randomUserPagingDataSource)
}