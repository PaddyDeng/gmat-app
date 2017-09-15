package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect;


import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;

public class CollectionLectureFragment extends BaseCollectionFragment{

    @Override
    protected boolean conformCondition(PracticeRecordData data) {
        return data.getTwoObjectId() == 12;
    }
}
