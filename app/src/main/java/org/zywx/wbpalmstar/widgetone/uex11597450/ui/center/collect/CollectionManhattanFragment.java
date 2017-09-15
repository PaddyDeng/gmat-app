package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;

/**
 * Created by fire on 2017/8/9  11:34.
 */

public class CollectionManhattanFragment extends BaseCollectionFragment {
    @Override
    protected boolean conformCondition(PracticeRecordData data) {
        return data.getTwoObjectId() == 9;
    }
}