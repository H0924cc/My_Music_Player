package com.cc4cin.mymusicplayer2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class NetWorkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var network =manager.activeNetwork
        if(network==null) {
            Toast.makeText(context, "无网络连接", Toast.LENGTH_LONG).show()
            println("没网了")
        }
    }
}