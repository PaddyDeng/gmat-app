package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.knowpoint;


import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.KnowsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

public class SCKnowPrFragment extends BaseKnowPrFragment {

    @Override
    protected boolean conformCondition(KnowsData data) {
        return data.getKnowssectionid()== C.SC;
    }

    @Override
    protected int getKnowssectionid() {
        return C.SC;
    }
}
