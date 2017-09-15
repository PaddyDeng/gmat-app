package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.thrlib.RecognizeService;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.FileUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class CameraSearchActivity extends BaseActivity {


    @BindView(R.id.camera_crop_iv)
    ImageView cameraIv;
    @BindView(R.id.camera_result_tv)
    TextView recognResultTv;
    @BindView(R.id.recognize_camera_pb)
    ProgressBar mProgressBar;
    @BindView(R.id.question_search_input)
    EditText questionEt;
    private String content;

    public static void startAct(Context c, String content) {
        Intent intent = new Intent(c, CameraSearchActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_layout);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public BaseActivity.AnimType getAnimType() {
        return BaseActivity.AnimType.ANIM_TYPE_UP_IN;
    }

    @Override
    protected void initView() {
        cameraIv.setImageBitmap(BitmapFactory.decodeFile(FileUtil.getSaveFile(mContext).getAbsolutePath()));
        initRecognizeResult();
        questionEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Utils.controlTvFocus(cameraIv);
                    PicSearchActivity.startAct(mContext, content.trim());
                }
            }
        });
        Utils.controlTvFocus(cameraIv);
    }

    private void initRecognizeResult() {

        RecognizeService.recGeneral(FileUtil.getSaveFile(mContext).getAbsolutePath(),
                new RecognizeService.ServiceListener() {
                    @Override
                    public void onResult(String result) {
                        Utils.setGone(mProgressBar);
                        content = result;
                        recognResultTv.setText(content);
                        questionEt.setText(content);
                        if (!TextUtils.isEmpty(content) && !TextUtils.equals("[283504] Network error", content)) {
                            PicSearchActivity.startAct(mContext, content.trim());
                        }
                    }
                });


    }

    @OnClick({R.id.question_search_cancel_btn, R.id.go_on_camera, R.id.camera_crop_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_crop_iv:
                Utils.setVisible(mProgressBar);
                initRecognizeResult();
                break;
            case R.id.go_on_camera:
                finish();
                break;
            // 点击“取消”按钮，及半透明背景，均退出页面
            case R.id.question_search_cancel_btn:
                forword(MainActivity.class);
                break;
        }
    }

    @Override
    protected boolean preBackExitPage() {
        forword(MainActivity.class);
        return super.preBackExitPage();
    }
}
