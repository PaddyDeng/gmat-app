package org.zywx.wbpalmstar.widgetone.uex11597450.http;

import android.util.SparseArray;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.zywx.wbpalmstar.widgetone.uex11597450.BuildConfig;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitProvider {
    public static String BASEURL = "http://www.gmatonline.cn/";
    private static String LOGINURL = "http://login.gmatonline.cn/";
    private static String TOEFLURL = "http://www.toeflonline.cn/";
//    public static String SMARTAPPLYURL = "http://smartapply.gmatonline.cn/";
    public static String SMARTAPPLYURL = "http://www.smartapply.cn/";
    //    public static String GOSSIPURL = "http://gossip.gmatonline.cn/";
    public static String GOSSIPURL = "http://bbs.viplgw.cn/";
    public static String VIPLGW = "http://open.viplgw.cn/";

    private static SparseArray<Retrofit> sparseArray = new SparseArray<>();

    private RetrofitProvider() {
    }

    public static Retrofit getInstance(@HostType.HostTypeChecker int hostType) {
        Retrofit instance = sparseArray.get(hostType);
        if (instance == null) {
            synchronized (RetrofitProvider.class) {
                if (instance == null) {
                    instance = SingletonHolder.create(hostType);
                    sparseArray.put(hostType, instance);
                }
            }
        }
        return instance;
    }

    private static class SingletonHolder {

        private static Retrofit create(@HostType.HostTypeChecker int type) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);

//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(interceptor);
//            }


            String url = null;
            switch (type) {
                case HostType.LOGIN_REGIST_HOST:
                    url = LOGINURL;
                    break;
                case HostType.BASE_URL_HOST:
                    url = BASEURL;
                    break;
                case HostType.TOEFL_URL_HOST:
                    url = TOEFLURL;
                    break;
                case HostType.GOSSIP_URL_HOST:
                    url = GOSSIPURL;
                    break;
                case HostType.SMARTAPPLY_URL_HOST:
                    url = SMARTAPPLYURL;
                    break;
                case HostType.VIPLGW_URL_HOST:
                    url = VIPLGW;
                    break;
            }
            builder.networkInterceptors().add(new CookiesInterceptor(GmatApplication.getInstance()));
            builder.networkInterceptors().add(new StethoInterceptor());

            return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

}
