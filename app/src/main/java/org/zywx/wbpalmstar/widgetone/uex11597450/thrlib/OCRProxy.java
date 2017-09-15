package org.zywx.wbpalmstar.widgetone.uex11597450.thrlib;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

public class OCRProxy {
    private OCRProxy() {
    }

    public static void initToken(Context context) {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
//                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, context, "70zHhz3fhaESNELQLnnSua0E", "tf3RP1FSmGTHLmqLSj3Aqniwpavir5j8");
    }

    public static void orcRelease() {
        // 释放内存资源
        OCR.getInstance().release();
    }

}
