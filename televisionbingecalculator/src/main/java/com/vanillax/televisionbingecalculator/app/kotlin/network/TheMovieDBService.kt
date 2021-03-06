package com.vanillax.televisionbingecalculator.app.kotlin.network

import android.content.Context
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.TVShowByIdResponse
import com.vanillax.televisionbingecalculator.app.tbc.Constants
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

/**
 * Created by mitchross on 4/14/18.
 */
interface TheMovieDBService {

    companion object {

        fun create( context: Context): TheMovieDBService {

//            val interceptor = HttpLoggingInterceptor()
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong() // 10 MiB
            val cache = Cache(File(context.cacheDir, "http"), SIZE_OF_CACHE)
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            var httpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(RewriteRequestInterceptor())
                    .addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(RewriteResponseCacheControlInterceptor())
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl("https://api.themoviedb.org/3/")
                    .build()
            return retrofit.create(TheMovieDBService::class.java)
        }
    }

    @GET("search/tv?" + "api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryTV(@Query("query") show: String): Single<QueryResponse>

    @GET("tv/{showId}" + "?api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryTVDetails(@Path("showId") showId: String): Single<TVShowByIdResponse>

    @GET("{searchType}/{showId}/credits" + "?api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryCast(@Path("showId") showId: String, @Path("searchType") searchType: String): Single<CastResponse>


    @GET("search/movie?" + "api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryMovie(@Query("query") show: String): Single<QueryResponse>

    @GET("movie/{showId}" + "?api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryMovieDetails(@Path("showId") showId: String): Single<TVShowByIdResponse>

    @GET("movie/{showId}/credits" + "?api_key=" + Constants.TVBC_Constants.API_KEY)
     fun queryMovieCast(@Path("showId") showId: String): Single<CastResponse>

}

