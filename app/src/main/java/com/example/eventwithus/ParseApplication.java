package com.example.eventwithus;

import android.app.Application;

import com.example.eventwithus.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Message.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("6xhT5ToW3AuefrGSdUr5PgSfEAoYT4lfuSoQbh6k")
                .clientKey("ULywQLvdMTIEtF4NprDpyxxry89PWRRxRKY9KTZW")
                .clientBuilder(builder)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
