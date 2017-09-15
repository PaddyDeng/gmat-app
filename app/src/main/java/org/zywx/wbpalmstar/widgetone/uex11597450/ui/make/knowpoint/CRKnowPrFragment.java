package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.knowpoint;


import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.KnowsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

public class CRKnowPrFragment extends BaseKnowPrFragment {

    @Override
    protected boolean conformCondition(KnowsData data) {
        return data.getKnowssectionid()== C.CR;
    }

    @Override
    protected int getKnowssectionid() {
        return C.CR;
    }
}
