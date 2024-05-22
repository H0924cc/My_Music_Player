package com.cc4cin.mymusicplayer2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MusicActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var playPauseButton: Button
    lateinit var nextButton: Button
    lateinit var previousButton: Button

    lateinit var netWorkRecevier :NetWorkReceiver

    private lateinit var musicService: MusicService
    private val musicConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            // 现在可以安全地与服务进行交互
        }

        override fun onServiceDisconnected(name: ComponentName) {
            //musicService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_music)

        // 绑定服务
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)

        // 初始化播放控制按钮并设置点击事件监听器
        playPauseButton = findViewById(R.id.btn_startOrStop)
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_post)

        playPauseButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        previousButton.setOnClickListener(this)

        //
        netWorkRecevier = NetWorkReceiver() // intentRecevier定义为全局变量
        var filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        this.registerReceiver(netWorkRecevier,filter)

    }


    override fun onDestroy() {
        super.onDestroy()
        // 解除服务绑定
        unbindService(musicConnection)
        unregisterReceiver(netWorkRecevier)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_startOrStop -> {
                    Toast.makeText(this,"播放/暂停",Toast.LENGTH_SHORT).show()
                    musicService.playPause()
                }
                R.id.btn_next -> {
                    Toast.makeText(this,"下一首",Toast.LENGTH_SHORT).show()
                    musicService.nextSong()
                }
                R.id.btn_post -> {
                    Toast.makeText(this,"上一首",Toast.LENGTH_SHORT).show()
                    println("上一首按钮被点击")
                    musicService.previousSong()
                }
            }
        }
    }
}