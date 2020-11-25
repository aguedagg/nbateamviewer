package com.aguedagg.nbateamviewer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.aguedagg.nbateamviewer.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.core.widget.ContentLoadingProgressBar;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = "ApiClient";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    private static Retrofit retrofit;
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private static Context mContext;

    public static Retrofit getClient(Context context){
        mContext = context;

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build();
    }

    private static Cache cache(){
        return new Cache(new File(mContext.getCacheDir(),"someIdentifier"), cacheSize);
    }

    private static Interceptor offlineInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(TAG, "offline interceptor: called.");
                Request request = chain.request();

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!isNetworkConnected()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    private static Interceptor networkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(TAG, "network interceptor: called.");

                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build();

                return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger() {
            @Override
            public void log (String message) {
                Log.d(TAG, "log: http log: " + message);
            }
        });
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
