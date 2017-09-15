package org.zywx.wbpalmstar.widgetone.uex11597450.http;


import android.content.Context;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CookiesInterceptor implements Interceptor {

    private Context mContext;

    public CookiesInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request compressedRequest;
        compressedRequest = request.newBuilder()
                .header("cookie", SharedPref.getCookie(mContext))
                .build();
        Response originalResponse = chain.proceed(compressedRequest);

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
                SharedPref.saveCookie(mContext, header);
            }
        }
        return originalResponse;
    }
}
