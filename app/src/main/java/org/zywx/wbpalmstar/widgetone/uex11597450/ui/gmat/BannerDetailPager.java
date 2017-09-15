package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

public class BannerDetailPager extends Fragment {
    public static final String URL = "url";
    private ImageView imageView;
    private String mUrl;

    public static BannerDetailPager getInstance(String url) {
        BannerDetailPager imageDetails = new BannerDetailPager();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        imageDetails.setArguments(bundle);
        return imageDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrl = savedInstanceState == null ? getArguments().getString(URL) : savedInstanceState.getString(URL);
        if (TextUtils.isEmpty(mUrl)) {
            imageView.setBackgroundResource(R.drawable.place_img);
        } else {
            GlideUtil.loadDefault(mUrl, imageView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(URL, mUrl);
    }
}
