package og.ogstartracker.di

import com.squareup.moshi.Moshi
import og.ogstartracker.BuildConfig
import og.ogstartracker.network.ArduinoApi
import og.ogstartracker.network.EmptyBodyConverterFactory
import og.ogstartracker.network.UnitConverterFactory
import og.ogstartracker.repository.ArduinoRepository
import og.ogstartracker.repository.ArduinoRepositoryImpl
import og.ogstartracker.repository.DataStoreRepository
import og.ogstartracker.repository.DataStoreRepositoryImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val NETWORK_REQUEST_TIMEOUT = 30L
const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"

val networkModule = module {

	fun provideRetrofit(
		url: String,
		scope: Scope,
		interceptors: List<Interceptor>,
	): Retrofit {
		val builder = OkHttpClient.Builder()
		interceptors.forEach {
			builder.addInterceptor(it)
		}
		builder.connectTimeout(NETWORK_REQUEST_TIMEOUT, TimeUnit.SECONDS)
		builder.readTimeout(NETWORK_REQUEST_TIMEOUT, TimeUnit.SECONDS)
		builder.writeTimeout(NETWORK_REQUEST_TIMEOUT, TimeUnit.SECONDS)
		val client = builder.build()

		return Retrofit.Builder()
			.baseUrl(url)
			.client(client)
			.addConverterFactory(EmptyBodyConverterFactory)
			.addConverterFactory(UnitConverterFactory)
			.addConverterFactory(
				MoshiConverterFactory.create(scope.get()).withNullSerialization()
			)
			.build()
	}

	single {
		provideRetrofit(
			url = BuildConfig.TRACKER_URL,
			scope = this,
			interceptors = listOf(
				get(named(HTTP_LOGGING_INTERCEPTOR))
			),
		).create(ArduinoApi::class.java)
	}

	single(named(HTTP_LOGGING_INTERCEPTOR)) {
		val logging = HttpLoggingInterceptor.Logger { message ->
			Timber.tag("OkHttp").d(message)
		}
		HttpLoggingInterceptor(logging).apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
	} bind Interceptor::class

	single {
		OkHttpClient.Builder()
			.apply {
				addInterceptor(get<HttpLoggingInterceptor>())
			}
			.build()
	}

	single {
		HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
	}

	single<ArduinoRepository> {
		ArduinoRepositoryImpl(
			arduinoApi = get()
		)
	}

	single<DataStoreRepository> {
		DataStoreRepositoryImpl(
			context = androidContext()
		)
	}

	single {
		Moshi.Builder().build()
	}
}