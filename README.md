# DownloadButton
一个下载的button

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.mouse12138:DownloadButton:1.0.0'
	}



![](https://i.imgur.com/s6sPwqR.gif)





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
