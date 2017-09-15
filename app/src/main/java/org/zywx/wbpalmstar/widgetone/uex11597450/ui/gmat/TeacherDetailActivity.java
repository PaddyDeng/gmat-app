package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.TeacherData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class TeacherDetailActivity extends BaseActivity {

    public static void startTeacherDetail(Context c, TeacherData data) {
        Intent intent = new Intent(c, TeacherDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        c.startActivity(intent);
    }

    @BindView(R.id.teacher_detail_title)
    TextView titleTv;
    @BindView(R.id.teacher_detail_title_tv)
    TextView contTitle;
    @BindView(R.id.teacher_detail_des_tv)
    TextView detailDes;
    @BindView(R.id.teacher_detail_item_img)
    CircleImageView headIv;
    @BindView(R.id.teacher_detail_info)
    TextView detailInfo;

    private TeacherData mTeacherData;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            mTeacherData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
    }

    @Override
    protected void initView() {
        if (mTeacherData == null) return;
        titleTv.setText(mTeacherData.getTeacherName());
        contTitle.setText(mTeacherData.getTeacherName());
        detailDes.setText(mTeacherData.getTitle());
        GlideUtil.loadDefault(RetrofitProvider.BASEURL + mTeacherData.getTeacherIamge(), headIv, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        detailInfo.setText(HtmlUtil.fromHtml(HtmlUtil.replaceSpace(HtmlUtil.fromHtml(mTeacherData.getIntroduce()).toString())));
    }

    @OnClick({R.id.teacher_course_reservation})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_course_reservation:
                if (mTeacherData != null)
                    PreProGramLessonActivity.startPre(mContext, mTeacherData.getTeacherId(), mTeacherData.getTitle());
                break;
            default:
                break;
        }
    }

}
