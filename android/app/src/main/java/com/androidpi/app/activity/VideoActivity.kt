package com.androidpi.app.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androidpi.app.R

class VideoActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, options: Bundle?) {
            val intent = Intent(context, VideoActivity::class.java)
            context.startActivity(intent, options)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
    }

}
