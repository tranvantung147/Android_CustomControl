package ms.com.extensionandlib

import com.google.gson.GsonBuilder
import ms.com.extensionandlib.livedata.retrofit.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

object APIManager {
    var client: Retrofit? = null

    var url: String by Delegates.observable("") { _, _, newValue ->
        if (newValue.isNotEmpty()) {
            client = createRetrofit()
        }
    }

    private fun createRetrofit(): Retrofit? {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val loggingInterceptor =
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }

        val okHttpClient = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor).build()

        val gson = GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()
        return Retrofit.Builder().baseUrl(if (url.trimEnd() == "/") url else "$url/")
            .client(okHttpClient).addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    fun resetBaseUrl(urlReset: String) {
        url = urlReset
    }
}