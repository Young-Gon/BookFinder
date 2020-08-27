package com.gondev.bookfinder.di

import com.gondev.bookfinder.BuildConfig
import com.gondev.bookfinder.model.network.api.BookAPI
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L

/**
 * 네트워크 관련 모귤 등록 변수 입니다.
 */
val networkModule = module {
	single { Cache(androidApplication().cacheDir, 10 * 1024 * 1024) }

	single {
		Interceptor { chain ->
			chain.proceed(chain.request().newBuilder().build())
		}
	}

	single {
		OkHttpClient.Builder().apply {
			cache(get())
			connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
			writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
			readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
			retryOnConnectionFailure(true)
			addInterceptor(get<Interceptor>())
			addInterceptor(HttpLoggingInterceptor().apply {
				if (BuildConfig.DEBUG) {
					level = HttpLoggingInterceptor.Level.BODY
				}
			})
		}.build()
	}

	single {
		Retrofit.Builder()
			.baseUrl(BuildConfig.url_base)
			.addConverterFactory(GsonConverterFactory.create())
			.client(get())
			.build()
	}

	single { get<Retrofit>().create(BookAPI::class.java) }
}