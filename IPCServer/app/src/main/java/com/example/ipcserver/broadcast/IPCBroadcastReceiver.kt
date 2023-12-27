package com.example.ipcserver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ipcserver.DATA
import com.example.ipcserver.PACKAGE_NAME
import com.example.ipcserver.PID
import com.example.ipcserver.data.Client
import com.example.ipcserver.data.RecentClient

class IPCBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        RecentClient.client = intent?.getStringExtra(PID)?.let {
            Client(
                intent?.getStringExtra(PACKAGE_NAME),
                it,
                intent?.getStringExtra(DATA),
                "AIDL"
            )
        }
    }
}