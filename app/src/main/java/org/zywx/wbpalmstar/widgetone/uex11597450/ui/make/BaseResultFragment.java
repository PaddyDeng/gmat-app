package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;

public abstract class BaseResultFragment extends BaseFragment {
    /**
     * 结果详情是否显示
     *
     * @return true 显示的
     */
    public abstract boolean isShow();

    /**
     * 切换答案详情显示与隐藏
     */
    public abstract void toggleAnswerDetailContainer();

    /**
     * 问题是否被收藏
     * return true 是被收藏的
     */
    public boolean isCollection() {
        return PracticeManager.getInstance().queryWhetherCollection(getQuestionId());
    }

    /**
     * 返回问题id，用于收藏
     */
    public abstract int getQuestionId();
}
