package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TestType {
    @IntDef({SINGLE_PRACTICE, INTELLIGENT_TEST, KNOWS_TEST, DIFF_TEST, SEARCH_QUESTION,ORDER_TEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TestTypeChecker {
    }

    /*
    * 单项练习
    */
    @TestType.TestTypeChecker
    public static final int SINGLE_PRACTICE = 1;
    /*
    * 智能学习
    */
    @TestType.TestTypeChecker
    public static final int INTELLIGENT_TEST = 2;
    /*
    * 知识点
    */
    @TestType.TestTypeChecker
    public static final int KNOWS_TEST = 3;
    /*
    * 难度
    */
    @TestType.TestTypeChecker
    public static final int DIFF_TEST = 4;
    /*
    * 来自查询题目
    */
    public static final int SEARCH_QUESTION = 5;
    /*
    * 顺序做题
    */
    public static final int ORDER_TEST = 6;
}
