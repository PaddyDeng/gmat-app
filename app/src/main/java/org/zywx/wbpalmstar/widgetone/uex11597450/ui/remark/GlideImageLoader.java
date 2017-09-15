package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.widget.ImageView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caimuhao.rxpicker.utils.RxPickerImageLoader;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;

public class GlideImageLoader implements RxPickerImageLoader {

    @Override
    public void display(ImageView imageView, String path, int width, int height) {

        GlideUtil.loadDefaultOverrideNoAnim(path, imageView, width, height, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);

    }
}
