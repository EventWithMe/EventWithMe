package com.example.eventwithus;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("6xhT5ToW3AuefrGSdUr5PgSfEAoYT4lfuSoQbh6k")
                .clientKey("ULywQLvdMTIEtF4NprDpyxxry89PWRRxRKY9KTZW")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
