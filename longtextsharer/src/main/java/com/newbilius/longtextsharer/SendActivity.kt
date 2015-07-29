package com.newbilius.longtextsharer

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.newbilius.longtextsharer.R
import kotlinx.android.synthetic.activity_send.*
import org.jetbrains.anko.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI

public class SendActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_action_about -> {
                alert(R.string.about_alert_text, R.string.about_alert_title) {
                    positiveButton(R.string.about_alert_other_progs) {
                        browse("https://play.google.com/store/apps/developer?id=%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9+%D0%9C%D0%BE%D0%B8%D1%81%D0%B5%D0%B5%D0%B2")
                    }
                }.show()
            }
        }
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        editText.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        buttonSend.setOnClickListener {
            val text = editText.getText().toString();
            if (text.length() == 0) {
                alertError(R.string.error_empty_text);
                return@setOnClickListener;
            }

            val bitmap = writeTextToImage(text, 460 x 780, 36, 10);

            try {
                val cachePath = File(getExternalCacheDir(), "temp");
                cachePath.mkdirs();
                val fileName = "$cachePath/long_text_image.png";
                val stream = FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                stream.close();

                val imageView = ImageView(this);

                imageView.setImageBitmap(bitmap);

                alert(R.string.preview_alert_title) {
                    customView(imageView)
                    positiveButton(R.string.preview_alert_ok) {
                        share(R.string.app_name, "image/png", fileName)
                    }
                    negativeButton(R.string.preview_alert_cancel) { dismiss() }
                }.show()

            } catch (e: FileNotFoundException) {
                alertError(R.string.error_iofile);
                e.printStackTrace();
            } catch (e: IOException) {
                alertError(R.string.error_iofile);
                e.printStackTrace();
            }
        };
    }

    fun alertError(resourceId: Int) {
        alert(resourceId, R.string.error_title) {
            positiveButton(R.string.ok) { dismiss() }
        }.show()
    }

    // Inspired by Context.share() from anko ContextUtils.kt
    // Probably should make it more generic and propose for including in anko
    fun share(title: Int, type: String, file: String): Boolean {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType(type)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(file)))
            startActivity(Intent.createChooser(intent, getText(title)))
            return true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            return false
        }
    }
}