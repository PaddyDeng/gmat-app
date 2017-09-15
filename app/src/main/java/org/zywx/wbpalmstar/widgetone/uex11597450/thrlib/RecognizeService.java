/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package org.zywx.wbpalmstar.widgetone.uex11597450.thrlib;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;

import java.io.File;

public class RecognizeService {

    public interface ServiceListener {
        void onResult(String result);
    }

    public static void recGeneral(String filePath, final ServiceListener listener) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setVertexesLocation(true);
        param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL);
        param.setImageFile(new File(filePath));
        if (OCR.getInstance() == null) {
            listener.onResult("");
        }
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                if (result == null) {
                    listener.onResult("");
                }
                StringBuilder sb = new StringBuilder();
                for (WordSimple wordSimple : result.getWordList()) {
                    Word word = (Word) wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }

                listener.onResult(sb.toString());
            }

            @Override
            public void onError(OCRError error) {
                listener.onResult(error.getMessage());
            }
        });
    }


}
