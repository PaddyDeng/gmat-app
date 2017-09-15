package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.os.Bundle;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.SimpleShowDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    @OnClick({R.id.gov_wx, R.id.sina_online, R.id.gmat_service_email, R.id.gmat_service_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gov_wx:
                Utils.copy(getString(R.string.str_contact_us_gov_wx_detal), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.sina_online:
                Utils.copy(getString(R.string.str_contact_us_online), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.gmat_service_email:
                Utils.copy(getString(R.string.str_contact_us_email_detail), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.gmat_service_phone:
                call();
                break;
        }
    }

    private void call() {
        SimpleShowDialog.getInstance(getString(R.string.str_contact_us_contact_service), getString(R.string.str_contact_us_service_phone_num),
                new ICallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        callPhone("400 1816 180");
                    }

                    @Override
                    public void onFail() {

                    }
                }).showDialog(getSupportFragmentManager());
    }
}
