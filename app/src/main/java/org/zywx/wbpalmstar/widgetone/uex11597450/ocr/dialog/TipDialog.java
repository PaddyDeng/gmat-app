package org.zywx.wbpalmstar.widgetone.uex11597450.ocr.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ocr.util.SPUtils;

public class TipDialog extends DialogFragment {
    private View mRootView;
    private CheckBox mCheckBox;
    private TextView goOn;

    public interface CallBack {
        void goOn();
    }

    private static CallBack mCallback;

    public static TipDialog getInstance(CallBack mcallback) {
        TipDialog tipDialog = new TipDialog();
        mCallback = mcallback;
        return tipDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_tip_layout, container, false);
        mCheckBox = (CheckBox) mRootView.findViewById(R.id.tip_checkbox);
        goOn = (TextView) mRootView.findViewById(R.id.go_on);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.setBoolean("TIP_INFO", true, getActivity());
                } else {
                    SPUtils.setBoolean("TIP_INFO", false, getActivity());
                }
            }
        });
        goOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.goOn();
                    mCallback = null;
                }
                dismiss();
            }
        });
    }

    public Point getScreenSize(Context context) {

        // 获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        return screenSize;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallback != null) {
            mCallback = null;
        }
        if (null != mRootView) {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mRootView);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void dismiss() {
        if (null == getActivity()) {
            return;
        }
        super.dismiss();
    }

    public void showDialog(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(TipDialog.this, "dialog");
        ft.commitAllowingStateLoss();
    }
}
