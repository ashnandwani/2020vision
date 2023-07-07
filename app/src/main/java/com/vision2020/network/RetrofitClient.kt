package com.vision2020.network
import com.vision2020.AppApplication
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
object RetrofitClient {
    private val CACHE_CONTROL = "Cache-Control"
    private var retrofit: Retrofit? = null
   // private var retrofitGoogle: Retrofit? = null
  //  private var retrofitEtaGoogle: Retrofit? = null
    private var apiInterface: ApiInterface? = null
    private var baseUrl = "http://ec2-15-206-63-201.ap-south-1.compute.amazonaws.com"

    val instance: ApiInterface?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        //.baseUrl(BuildConfig.BASE_URL)
                        .client(provideOkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            if (apiInterface == null) {
                apiInterface = retrofit!!.create(ApiInterface::class.java)
            }
            return apiInterface
        }
    //Creating OKHttpClient
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
               // .addInterceptor(provideOfflineCacheInterceptor())
               // .addNetworkInterceptor(provideCacheInterceptor())
                //.cache(provideCache())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    //Creating Cache
    private fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(
                    File(AppApplication.getInstance()!!.cacheDir, "http-cache"),
                    (10 * 1024 * 1024).toLong()
            ) // 10 MB
        } catch (ignored: Exception) {

        }

        return cache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build()


           // val authToken = Credentials.basic("token","wewbwe47vfbhbrhwhjrwjh")

//            for spanish --> es-UY
//            for en --> en-US
            response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                   // .addHeader("Authorization",authToken)
                   // .addHeader("accept-language", NETWORK_LOCALE_KEY)
                   // .addHeader("authorization", getAppPref().getString(InterConst.AUTH_TOKEN)!!)
                    .build()
        }
    }

    //Provides offline cache
    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if (AppApplication.hasNetwork()) {
                val cacheControl = CacheControl.Builder()
//                        .maxStale(7, TimeUnit.DAYS)
                        .build()

               // val authToken = Credentials.basic("test","itweuri7bhbhbfhdjdf")

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                       // .addHeader("Authorization",authToken)
                       //.addHeader("accept-language", NETWORK_LOCALE_KEY)
                       //.addHeader("authorization", getAppPref().getString(InterConst.AUTH_TOKEN)!!)
                        .build()
            }

            chain.proceed(request)
        }
    }

}
