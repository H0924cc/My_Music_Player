package com.cc4cin.mymusicplayer2

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import java.io.IOException

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val playlist = 6
    private var currentSongIndex = 0
    private var isPlaying = false

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        println("服务被创建")
        super.onCreate()
        mediaPlayer = MediaPlayer()
        initialDataSource()
    }

    private fun initialDataSource() {
        val assetManager = applicationContext.assets
        val fileName = "music/song$currentSongIndex.mp3"
        val fd = assetManager.openFd(fileName)
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer.prepare()
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }

    override fun onBind(intent: Intent?): IBinder {
        return MusicBinder()
    }


    fun playPause() {
        println("播放/暂停方法被调用")
        if (isPlaying) {
            println("暂停方法被调用")
            mediaPlayer.pause()
            isPlaying = false
        } else {
            println("播放方法被调用")
            mediaPlayer.start()
            isPlaying = true
        }
    }

    fun nextSong() {
        currentSongIndex = (currentSongIndex + 1) % playlist
        if(isPlaying) mediaPlayer.reset()
        initialDataSource()
        mediaPlayer.start()
    }

    fun previousSong() {
        currentSongIndex = ((currentSongIndex - 1 + playlist) % playlist)
        if(isPlaying) mediaPlayer.reset()
        initialDataSource()
        mediaPlayer.start()
    }

    private fun stopMusic() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        isPlaying = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }

    data class Song(val title: String)
}