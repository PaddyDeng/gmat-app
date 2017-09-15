package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RegexValidateUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class PreProGramLessonActivity extends BaseActivity {

    public static void startPre(Context c, String teachId, String lessonName) {
        Intent intent = new Intent(c, PreProGramLessonActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, teachId);
        intent.putExtra(Intent.EXTRA_TITLE, lessonName);
        c.startActivity(intent);
    }

    @BindView(R.id.prepro_et_phone)
    EditText proPhone;
    @BindView(R.id.prepro_et_name)
    EditText proName;

    private String teachId;
    private String lessonName;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        lessonName = intent.getStringExtra(Intent.EXTRA_TITLE);
        teachId = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pre_pro_gram_lesson);
    }

    @OnClick({R.id.place_one_view, R.id.place_two_view, R.id.place_thr_view, R.id.place_close,
            R.id.prepro_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.place_close:
            case R.id.place_one_view:
            case R.id.place_two_view:
            case R.id.place_thr_view:
                finishWithAnim();
                break;
            case R.id.prepro_commit:
                commit();
                break;
            default:
                break;
        }
    }

    private void commit() {
        String name = getEditText(proName);
        String phone = getEditText(proPhone);
        if (TextUtils.isEmpty(name)) {
            toastShort(R.string.str_enter_you_name_tip);
            return;
        }
        if (!RegexValidateUtil.checkPhoneNumber(phone)) {
            toastShort(R.string.str_phone_error_tip);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toastShort(R.string.str_enter_you_phone_tip);
            return;
        }
//        List<String> extend = new ArrayList<>();
//        extend.add(teachId);
//        extend.add(phone);
//        extend.add(lessonName);//课程名称
//        extend.add("android gmat app");//来源
//        Map<String, Object> map = new HashMap<>();
//        map.put("catId", 236);
//        map.put("name", name);
//        map.put("extend", extend);
//        addToCompositeDis(HttpUtil.addContentToMap(map).subscribe(new Consumer<ResultBean>() {
//            @Override
//            public void accept(@NonNull ResultBean bean) throws Exception {
//                if (getHttpResSuc(bean.getCode())) {
//                    finishWithAnim();
//                } else {
//                    toastShort(bean.getMessage());
//                }
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//            }
//        }));
        String[] arr = new String[]{teachId.trim(),phone.trim(),lessonName.trim(),"android gmat app"};
        addToCompositeDis(HttpUtil.addContent(name, arr).subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                if (getHttpResSuc(bean.getCode())) {
                    finishWithAnim();
                } else {
                    toastShort(bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        }));
    }


    @Override
    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_UP_IN;
    }
}
