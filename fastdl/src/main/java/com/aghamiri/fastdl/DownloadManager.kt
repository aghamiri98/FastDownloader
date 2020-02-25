package com.aghamiri.fastdl

import android.os.AsyncTask
import java.io.*
import java.net.URL

class DownloadManager(private val downloadLink: String, private val downloadPath: String, private val onDownloadProgressListener: OnDownloadProgressListener) : AsyncTask<String?, String?, String?>() {
    private var total: Long = 0
    private var fail = false


    override fun onCancelled() {
        super.onCancelled()
        onDownloadProgressListener.downloadCancel()
    }

    override fun onPreExecute() {
        super.onPreExecute()
        onDownloadProgressListener.downloadStart()
    }


    override fun onProgressUpdate(vararg values: String?) {}

    override fun onPostExecute(file_url: String?) {
        if (fail) {
            onDownloadProgressListener.downloadFail("download fail")
        } else {
            onDownloadProgressListener.downloadedSuccess()
        }
    }

    private val fileName=downloadLink.split("/").toTypedArray()[downloadLink.split("/").toTypedArray().size - 1]

    override fun doInBackground(vararg params: String?): String? {
        var count: Int
        try {
            val url = URL(downloadLink)
            val connection = url.openConnection()
            connection.connect()
            val lengthOfFile = connection.contentLength
            val input: InputStream = BufferedInputStream(url.openStream(), 8192)
            val file = File(downloadPath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val output: OutputStream =
                FileOutputStream("$downloadPath/$fileName")
            val data = ByteArray(1024)
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                publishProgress("" + (total * 100 / lengthOfFile).toInt())
                onDownloadProgressListener.percent((total * 100 / lengthOfFile).toInt())
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
            fail = false
        } catch (e: Exception) {
            fail = true
            onDownloadProgressListener.downloadFail(e.message)
        }
        return null

    }

}