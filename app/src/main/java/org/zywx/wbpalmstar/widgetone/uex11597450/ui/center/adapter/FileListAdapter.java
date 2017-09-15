package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileInfoData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileLevel;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileTypeData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.OpenFileUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.ItemSlideHelper;

import java.io.File;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> /*implements ItemSlideHelper.Callback*/ {

    private static final int ONE_ITEM = 1;
    private static final int TWO_ITEM = 2;

    private List<FileLevel> mFileLevels;
//    private RecyclerView mRecyclerView;

    public FileListAdapter(List<FileLevel> fileInfoDataList) {
        mFileLevels = fileInfoDataList;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == ONE_ITEM) {
            return new BaseRecyclerViewHolder(context, LayoutInflater.from(context).inflate(R.layout.file_one_level_item_layout, parent, false));
        } else if (viewType == TWO_ITEM) {
            return new BaseRecyclerViewHolder(context, LayoutInflater.from(context).inflate(R.layout.file_two_level_item_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        final int type = getItemViewType(position);
        final Context context = holder.itemView.getContext();
        if (type == ONE_ITEM) {//加载一级目录,点击打开或关闭二级目录
            FileTypeData data = (FileTypeData) mFileLevels.get(position);
            holder.getTextView(R.id.file_item_des).setText(data.getTypeStr());
            final ImageView imageView = holder.getImageView(R.id.show_file_list_one_level_iv);
            holder.getView(R.id.one_level_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    FileTypeData typeData = (FileTypeData) mFileLevels.get(adapterPosition);
                    List<FileInfoData> dataList = typeData.getFileInfoDataList();
                    if (typeData.isExpand()) {//是打开，要关闭
                        typeData.setExpand(false);
                        if (dataList != null && !dataList.isEmpty()) {
                            mFileLevels.removeAll(dataList);
                            notifyItemRangeRemoved(adapterPosition + 1, dataList.size());
                        }
                        rotationExpandIcon(imageView, 0, -90);
                    } else {//关闭，要打开
                        typeData.setExpand(true);
                        if (dataList != null && !dataList.isEmpty()) {
                            mFileLevels.addAll(adapterPosition + 1, dataList);
                            notifyItemRangeInserted(adapterPosition + 1, dataList.size());
                        }
                        rotationExpandIcon(imageView, -90, 0);
                    }
                }
            });
        } else if (type == TWO_ITEM) {//点击二级目录，查看对应文件
            final FileInfoData infoData = (FileInfoData) mFileLevels.get(position);
            holder.getTextView(R.id.tow_level_item_title_tv).setText(infoData.getTitle());
            holder.getTextView(R.id.two_level_item_file_size_tv).setText(infoData.getFileSize());
            holder.getTextView(R.id.two_level_item_file_modify_time_tv).setText(infoData.getUpdateTime());
            holder.getView(R.id.two_level_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenFileUtil.openFile(new File(infoData.getFilePath()), context);
                }
            });
            holder.getTextView(R.id.two_level_item_delete_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(infoData.getFilePath());
                    if (file.exists()) {
                        file.delete();
                    }
                    mFileLevels.remove(infoData);
                    notifyDataSetChanged();
                }
            });
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(final ImageView expand, float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    expand.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mFileLevels.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mFileLevels == null ? 0 : mFileLevels.size();
    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        mRecyclerView = recyclerView;
//        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
//    }
//
//    @Override
//    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
//        if (holder.itemView instanceof LinearLayout) {
//            ViewGroup viewGroup = (ViewGroup) holder.itemView;
//            if (viewGroup.getChildCount() == 2) {
//                return viewGroup.getChildAt(1).getLayoutParams().width;
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
//        return mRecyclerView.getChildViewHolder(childView);
//    }
//
//    @Override
//    public View findTargetView(float x, float y) {
//        return mRecyclerView.findChildViewUnder(x, y);
//    }
}
