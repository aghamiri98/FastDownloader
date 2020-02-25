package com.aghamiri.fastdownloader

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aghamiri.fastdl.DownloadManager
import com.aghamiri.fastdl.OnDownloadProgressListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
                downloadStart()
            }

        }
    }

    private fun downloadStart() {
        val downloadLink = "https://hw16.cdn.asset.aparat.com/aparat-video/be1844ca1fec7583ed80677419d78bcb19660635-480p__90599.mp4"
        //val downloadPath = this.getExternalFilesDir("fastDownload")!!.absolutePath
        val downloadPath = "/sdcard/fastDl"
        val dlManager = DownloadManager(downloadLink, downloadPath,
            object : OnDownloadProgressListener {
                override fun downloadStart() {
                    logi("downloadStart")
                }

                override fun downloadedSuccess() {
                    logi("downloadedSuccess")
                    txtPercent.text="download successful"
                }

                override fun downloadCancel() {
                    loge("downloadCancel")
                }

                override fun downloadFail(error: String?) {
                    loge("downloadFail:$error")
                }

                override fun percent(percent: Int) {
                    runOnUiThread {
                        progressBar.progress=percent
                        txtPercent.text="$percent%"
                        logd("downloadProgress:$percent")

                    }

                }
            })

        dlManager.execute()


    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("agree", okListener)
            .setNegativeButton("cancel", null)
            .create()
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val writeExternalStorageAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeExternalStorageAccepted) {
                    Snackbar.make(
                        window.decorView.rootView,
                        "Allow Access external Storage",
                        Snackbar.LENGTH_LONG
                    ).show()
                    downloadStart()
                } else {
                    Snackbar.make(
                        window.decorView.rootView,
                        "Deny Access external Storage",
                        Snackbar.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("you should access external storage",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(WRITE_EXTERNAL_STORAGE),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }
                    }

                }
            }
        }
    }

}
