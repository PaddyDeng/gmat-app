package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileInfoData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileLevel;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.FileTypeData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.FileListAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;
import zlc.season.rxdownload2.function.Utils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class FileListActivity extends BaseActivity {
    @BindView(R.id.file_list_recycle)
    RecyclerView mRecyclerView;
    private FileListAdapter mAdapter;
    private RxDownload mRxDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
    }

    @Override
    protected void initData() {
        super.initData();
        mRxDownload = RxDownload.getInstance(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initFileData();
    }

    private void initFileData() {
        mRxPermissions.request(READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                }).compose(new ObservableTransformer<Boolean, List<DownloadRecord>>() {
            @Override
            public ObservableSource<List<DownloadRecord>> apply(Observable<Boolean> upstream) {
                return upstream.flatMap(new Function<Boolean, ObservableSource<List<DownloadRecord>>>() {
                    @Override
                    public ObservableSource<List<DownloadRecord>> apply(@NonNull Boolean aBoolean) throws Exception {
                        return mRxDownload.getTotalDownloadRecords();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<DownloadRecord>>())
                .subscribe(new Consumer<List<DownloadRecord>>() {
                    @Override
                    public void accept(@NonNull List<DownloadRecord> records) throws Exception {
                        initFileListAdapter(records);
                    }
                });
    }

    private void initFileListAdapter(List<DownloadRecord> records) {
        List<FileInfoData> pdfLists = new ArrayList<>();
        List<FileInfoData> excelLists = new ArrayList<>();
        List<FileInfoData> wordLists = new ArrayList<>();
        List<FileInfoData> otherLists = new ArrayList<>();
        for (DownloadRecord record : records) {
            if (record.getFlag() != DownloadFlag.COMPLETED) {
                continue;
            }
            String filePath = TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString();
            File file = new File(filePath);
            if (!file.exists()) {
                continue;
            }
            String title = record.getExtra1();//带有后缀名的title
            FileInfoData infoData = new FileInfoData();
            infoData.setUrl(record.getUrl());
            infoData.setTitle(title);
            infoData.setFilePath(filePath);
            infoData.setFileSize(Utils.formatSize(file.length()));
            infoData.setUpdateTime(TimeUtils.longToString(file.lastModified(), "yyyy.MM.dd"));
            if (isPdf(title)) {
                //添加到pdf中
                pdfLists.add(infoData);
            } else if (isWord(title)) {
                //添加到word中
                wordLists.add(infoData);
            } else if (isExcel(title)) {
                excelLists.add(infoData);
            } else {
                otherLists.add(infoData);
            }
        }
        mRecyclerView.setAdapter(getFileListAdapter(pdfLists, excelLists, wordLists, otherLists));
    }

    private boolean isPdf(String title) {
        if (title.toLowerCase().endsWith(".pdf")) {
            return true;
        }
        return false;
    }

    private boolean isWord(String title) {
        title = title.toLowerCase();
        if (title.endsWith(".doc") || title.endsWith(".docx")) {
            return true;
        }
        return false;
    }

    private boolean isExcel(String title) {
        title = title.toLowerCase();
        if (title.endsWith(".xls") || title.endsWith(".xlsx")) {
            return true;
        }
        return false;
    }

    public RecyclerView.Adapter getFileListAdapter(List<FileInfoData>... lists) {
        List<FileLevel> mDatas = new ArrayList<>();
        FileTypeData data1 = new FileTypeData();
        data1.setTypeStr(getString(R.string.str_pdf));
        data1.setFileInfoDataList(lists[0]);
        mDatas.add(data1);
        mDatas.addAll(lists[0]);
        FileTypeData data2 = new FileTypeData();
        data2.setTypeStr(getString(R.string.str_excel));
        data2.setFileInfoDataList(lists[1]);
        mDatas.add(data2);
        mDatas.addAll(lists[1]);
        FileTypeData data3 = new FileTypeData();
        data3.setTypeStr(getString(R.string.str_word));
        data3.setFileInfoDataList(lists[2]);
        mDatas.add(data3);
        mDatas.addAll(lists[2]);
        FileTypeData data4 = new FileTypeData();
        data4.setTypeStr(getString(R.string.str_other));
        data4.setFileInfoDataList(lists[3]);
        mDatas.add(data4);
        mDatas.addAll(lists[3]);
        mAdapter = new FileListAdapter(mDatas);
        return mAdapter;
    }
}
