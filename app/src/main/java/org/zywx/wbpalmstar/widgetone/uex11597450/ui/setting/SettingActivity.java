package org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HeaderData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.DownloadApk;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.SimpleUpdateApk;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.SetNickNameActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.ModifyPhoneOrEmailDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.ModifyPwdDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.ImageUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CircleImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils.isEmpty;

public class SettingActivity extends BaseActivity implements DownloadApk.OnDownloadApkListener {

    @BindView(R.id.setting_user_name_tv)
    TextView userNameTv;
    @BindView(R.id.setting_nick_name_tv)
    TextView nickNameTv;
    @BindView(R.id.setting_phone_tv)
    TextView phoneTv;
    @BindView(R.id.setting_email_tv)
    TextView emailTv;
    @BindView(R.id.user_info_container)
    LinearLayout userInfoContainer;
    @BindView(R.id.exit_login)
    TextView exitTxt;
    @BindView(R.id.center_user_img)
    CircleImageView headIv;
    @BindView(R.id.set_modify_pwd_container)
    RelativeLayout pwdContainer;
    @BindView(R.id.current_version)
    TextView currentVersionTv;


    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;

    //调用照相机返回图片临时文件
    private File tempFile;

    //    private RxPermissions rxPermission;
    private SimpleUpdateApk mSimpleUpdateApk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        rxPermission = new RxPermissions(this);
        mSimpleUpdateApk = new SimpleUpdateApk(SettingActivity.this, true);
        createCameraTempFile(savedInstanceState);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.str_please_choose_pic)), REQUEST_PICK);
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case C.SET_NICK_REQUEST_CODE:
                nickNameTv.setText(GlobalUser.getInstance().getUserData().getNickname());
                //这里并不是退出登录，只是发送广播通知个人中心界面更新ui
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.EXIT_LOGIN_ACTION));
                RxBus.get().post(C.EXIT_LOGIN_ACTION, true);
                break;
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);

                    headIv.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    uploadHeader(cropImagePath);
                }
                break;
        }
    }

    private void uploadHeader(String cropImagePath) {
        File file = new File(cropImagePath);
        if (!file.exists()) {
            return;
        }
        RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mp = MultipartBody.Part.createFormData("upload", file.getName(), rb);
        addToCompositeDis(HttpUtil.replaceHeader(mp)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean<HeaderData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<HeaderData> bean) throws Exception {
                        dismissLoadDialog();
                        toastShort(bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            HeaderData data = bean.getData();
                            savePhoto(data.getPhotourl());
                            RxBus.get().post(C.CENTER_CHANGE, C.HEADER_CHANGE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }

    private void savePhoto(String photourl) {
        if (TextUtils.isEmpty(photourl)) return;
        GlobalUser.getInstance().setPhotoUrl(photourl);
        GlobalUser.getInstance().resetUserInfoToSp(mContext);
    }

    @OnClick({R.id.set_head_container, R.id.exit_login, R.id.set_nick_name_container, R.id.setting_user_name_container, R.id.set_modify_phone_container,
            R.id.set_modify_email_container, R.id.set_modify_pwd_container, R.id.set_gov_container, R.id.version_check, R.id.setting_font_size,
            R.id.set_wexin_container, R.id.set_tencent_qq_container, R.id.feed_back_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_font_size:
                forword(FontSizeSettingActivity.class);
                break;
            case R.id.version_check:
//                checkVersion();
                mSimpleUpdateApk.checkVersionUpdate();
                break;
            case R.id.set_head_container:
                headerChoose();
                break;
            case R.id.exit_login:
                //清空sp中的登录信息，清空内存中的个人对象
                GlobalUser.getInstance().exitLogin(mContext);
//                SharedPref.saveLoginInfo(mContext, "");
//                GlobalUser.getInstance().clearData();
//                SharedPref.saveCookie(mContext, "");
//                SharedPref.saveThrIcon(mContext, "");
//                SharedPref.saveThrId(mContext, "");
//                SharedPref.saveThrName(mContext, "");
//                try {
//                    if (PushProxy.isConnected(mContext))
//                        PushProxy.stopPush();
//                } catch (Exception e) {
//                    log(e.getMessage());
//                }
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.EXIT_LOGIN_ACTION));
                finishWithAnim();
                break;
            case R.id.setting_user_name_container:
                toastShort(R.string.str_set_modify_user_name_fail_tip);
                break;
            case R.id.set_nick_name_container:
                modifyNickName();
                break;
            case R.id.set_modify_phone_container:
                modifyPhoneOrEmail(false);
                break;
            case R.id.set_modify_email_container:
                modifyPhoneOrEmail(true);
                break;
            case R.id.set_modify_pwd_container:
                modifyPwd();
                break;
            case R.id.set_gov_container:
                Utils.copy(getString(R.string.str_set_gmat_net), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.set_wexin_container:
                Utils.copy(getString(R.string.str_set_wx_num_info), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.set_tencent_qq_container:
                Utils.copy(getString(R.string.str_set_gmat_qq_number), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.feed_back_container:
                forword(FeedBackActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSimpleUpdateApk != null) {
            mSimpleUpdateApk.onDestory();
            mSimpleUpdateApk = null;
        }
    }

    //    private void checkVersion() {
//        addToCompositeDis(HttpUtil.getUpdate()
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<VersionInfo>() {
//                    @Override
//                    public void accept(@NonNull final VersionInfo bean) throws Exception {
//                        dismissLoadDialog();
//                        if (Utils.getCurrentVersionNum(mContext) < bean.getVersions()) {
//                            //弹框提示用户是否需要更新
//                            tipUpadte(bean);
//                        } else {
//                            toastShort(R.string.str_is_new_apk);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                    }
//                }));
//    }

//    private void tipUpadte(final VersionInfo bean) {
//        UpdateApkDialog.getInstance(bean.getText(), new ICallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            new DownloadApk(SettingActivity.this).downloadApk(bean.getApk());
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFail() {
//            }
//        }).showDialog(getSupportFragmentManager());
//    }

    private void headerChoose() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    gotoCarema();
                                } else {
                                    toastShort(R.string.str_camera_no_permisson);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    gotoPhoto();
                                } else {
                                    toastShort(R.string.str_read_external_no_permisson);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void modifyPwd() {
        ModifyPwdDialog.getInstance(new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFail() {
            }
        }).showDialog(getSupportFragmentManager());
    }

    private void modifyPhoneOrEmail(final boolean modifyEmail) {
        ModifyPhoneOrEmailDialog.getInstance(modifyEmail, new ICallBack<String>() {
            @Override
            public void onSuccess(String emailOrPhone) {
                if (modifyEmail) {
                    emailTv.setText(emailOrPhone);
                } else {
                    phoneTv.setText(emailOrPhone);
                }
            }

            @Override
            public void onFail() {
            }
        }).showDialog(getSupportFragmentManager());
    }

    private void modifyNickName() {
        Intent intent = new Intent(mContext, SetNickNameActivity.class);
        startActivityForResult(intent, C.SET_NICK_REQUEST_CODE);
//        SimpleEditDialog.getInstance(new ICallBack<String>() {
//            @Override
//            public void onSuccess(String nickName) {
//                nickNameTv.setText(nickName);
////                setResult(RESULT_OK);
//                //这里并不是退出登录，只是发送广播通知个人中心界面更新ui
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.EXIT_LOGIN_ACTION));
//            }
//
//            @Override
//            public void onFail() {
//            }
//        }).showDialog(getSupportFragmentManager());
    }

    @Override
    protected void initData() {
        super.initData();
        currentVersionTv.setText(Utils.getCurrentVersion(mContext));
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            //有效
            UserData userData = GlobalUser.getInstance().getUserData();
            if (!TextUtils.isEmpty(userData.getUsername())) {
                userNameTv.setText(userData.getUsername());
            }
            if (!TextUtils.isEmpty(userData.getNickname())) {
                nickNameTv.setText(userData.getNickname());
            }
            if (!TextUtils.isEmpty(userData.getPhone())) {
                phoneTv.setText(userData.getPhone());
            }
            if (!TextUtils.isEmpty(userData.getUseremail())) {
                emailTv.setText(userData.getUseremail());
            }
            if (!TextUtils.isEmpty(userData.getPhoto())) {
//                headIv
                GlideUtil.loadDefault(RetrofitProvider.BASEURL + userData.getPhoto(), headIv, false,
                        DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
            }
            if (isEmpty(SharedPref.getPassword(mContext), SharedPref.getAccount(mContext))) {
                Utils.setGone(pwdContainer);
            }
        } else {
            Utils.setGone(userInfoContainer, exitTxt);
        }

    }

    @Override
    public void onDownError() {
        toastShort(R.string.str_update_apk_fail);
    }
}
