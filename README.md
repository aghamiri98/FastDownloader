FastDownloader a simple, powerful, file download manager library for Android.

<img src="https://github.com/aghamiri98/FastDownloader/blob/master/pics/shot1.png" width="200"><img src="https://github.com/aghamiri98/FastDownloader/blob/master/pics/shot2.png" width="200">

Features
--------

* Simple and easy to use API.
* Concurrent downloading support.
* Easy progress and status tracking.
* Save and Retrieve download information anytime.


How to use Fetch
----------------
```java
implementation 'com.github.aghamiri98:FastDownloader:1.0.0'
```

Next, get an instance of Fetch and request a download.

```kotlin
 val downloadLink = "https://hw16.cdn.asset.aparat.com/aparat-video/be1844ca1fec7583ed80677419d78bcb19660635-480p__90599.mp4"
        val downloadPath = "/sdcard/fastDl"
        val dlManager = DownloadManager(downloadLink, downloadPath,
            object : OnDownloadProgressListener {
                override fun downloadStart() {

                }

                override fun downloadedSuccess() {
                    txtPercent.text="download successful"
                }

                override fun downloadCancel() {

                }

                override fun downloadFail(error: String?) {

                }

                override fun percent(percent: Int) {
                    runOnUiThread {
                        progressBar.progress=percent
                        txtPercent.text="$percent%"
                    }

                }
            })

        dlManager.execute()

```

