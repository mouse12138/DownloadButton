# DownloadButton
一个下载的button
    
        mDownload5.setLineColor(5);
        mDownload5.setLineColor(Color.BLUE);
        
        mDownload1.setBtnType(DownloadButton.NORMAL);
        mDownload2.setBtnType(DownloadButton.DOWNLOAD);
        mDownload3.setBtnType(DownloadButton.FINISH);
        mDownload4.setBtnType(DownloadButton.END);
        
        
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
