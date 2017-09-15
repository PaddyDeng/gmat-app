package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.singlepractice;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;

public class PREPFragment extends BaseSinglePracticeFragment {
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
    protected int[] getTwoObjId() {
        return new int[]{8, 10, 11};
    }

}
