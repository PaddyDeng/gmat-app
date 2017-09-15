package org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.EvaluationDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

/**
 * Created by fire on 2017/8/31  10:10.
 */

public class EvaluationProxy {

    private EvaluationProxy() {
    }

    /**
     * 当做题数达到100,500,1000
     */
    public static boolean topicShowEvalu(String makeNum, Context context) {
        String eval = SharedPref.getEval(context);
        if (TextUtils.equals(eval, "100") || TextUtils.equals(eval, "500") || TextUtils.equals(eval, "1000")) {
            return false;
        }
        if (TextUtils.equals(makeNum, "100") || TextUtils.equals(makeNum, "500") || TextUtils.equals(makeNum, "1000")) {
            //显示好评
            SharedPref.setEval(context, makeNum);
            return true;
        }
        return false;
    }

    /**
     * 本地模考达到5题或10题时显示
     */
    public static void simulationShowEvalu(Context mContext, FragmentManager fragmentManager) {
        int simuEval = SharedPref.getSimuEval(mContext);
        if ((simuEval == 5 || simuEval == 10) && SharedPref.isCanShow(mContext)) {//已经做了5套题或10套题
            //已经显示，且没有做题，不能显示
            SharedPref.setCanShow(mContext, false);
            new EvaluationDialog().showDialog(fragmentManager);
        }
    }

}
