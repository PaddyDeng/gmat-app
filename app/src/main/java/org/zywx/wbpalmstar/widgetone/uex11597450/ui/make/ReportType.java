package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ReportType {
    @IntDef({ALL_REPORT, RC_REPORT, SC_REPORT, CR_REPORT,QUANT_REPORT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReportTypeCherker {
    }


    @ReportType.ReportTypeCherker
    public static final int ALL_REPORT = 1;

    @ReportType.ReportTypeCherker
    public static final int SC_REPORT = 2;

    @ReportType.ReportTypeCherker
    public static final int CR_REPORT = 3;

    @ReportType.ReportTypeCherker
    public static final int RC_REPORT = 4;

    @ReportType.ReportTypeCherker
    public static final int QUANT_REPORT = 5;
}
