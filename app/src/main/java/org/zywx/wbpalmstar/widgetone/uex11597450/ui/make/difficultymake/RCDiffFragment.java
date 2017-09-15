package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.difficultymake;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

public class RCDiffFragment extends BaseDiffFragment {
    private boolean viewCreate;

    @Override
    protected boolean isReceiver() {
        return viewCreate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreate = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
    }

    @Override
    protected int getSectionId() {
        return C.RC;
    }
}
