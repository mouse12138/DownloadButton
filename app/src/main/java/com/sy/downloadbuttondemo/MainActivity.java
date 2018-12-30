package com.sy.downloadbuttondemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sy.downloadbutton.DownloadButton;

public class MainActivity extends AppCompatActivity {

    private DownloadButton mDownload1;
    private DownloadButton mDownload2;
    private DownloadButton mDownload3;
    private DownloadButton mDownload4;
    private DownloadButton mDownload5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initType();
        initEvent();
    }

    private void initEvent() {
        mDownload5.setBtnClickListener(new DownloadButton.BtnClickListener() {
            @Override
            public void btnClick() {
                mDownload5.setBtnType(DownloadButton.DOWNLOAD);
                //耗时下载
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDownload5.setBtnType(DownloadButton.FINISH);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initType() {
        mDownload1.setBtnType(DownloadButton.NORMAL);
        mDownload2.setBtnType(DownloadButton.DOWNLOAD);
        mDownload3.setBtnType(DownloadButton.FINISH);
        mDownload4.setBtnType(DownloadButton.END);
    }

    private void initView() {
        mDownload1 = (DownloadButton) findViewById(R.id.download1);
        mDownload2 = (DownloadButton) findViewById(R.id.download2);
        mDownload3 = (DownloadButton) findViewById(R.id.download3);
        mDownload4 = (DownloadButton) findViewById(R.id.download4);
        mDownload5 = (DownloadButton) findViewById(R.id.download5);

        mDownload1.setLineColor(5);
        mDownload2.setLineColor(5);
        mDownload3.setLineColor(5);
        mDownload4.setLineColor(5);
        mDownload5.setLineColor(5);
        mDownload1.setLineColor(Color.BLUE);
        mDownload2.setLineColor(Color.RED);
        mDownload3.setLineColor(Color.GREEN);
        mDownload4.setLineColor(Color.YELLOW);
        mDownload5.setLineColor(Color.BLACK);
    }
}
