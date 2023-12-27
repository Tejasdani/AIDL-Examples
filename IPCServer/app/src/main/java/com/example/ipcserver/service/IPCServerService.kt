package com.example.ipcserver.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.text.TextUtils
import com.example.ipcserver.CONNECTION_COUNT
import com.example.ipcserver.DATA
import com.example.ipcserver.IMyAidlInterface
import com.example.ipcserver.PACKAGE_NAME
import com.example.ipcserver.PID
import com.example.ipcserver.data.Client
import com.example.ipcserver.data.RecentClient

class IPCServerService : Service() {
    companion object {
        private var connectionCount: Int = 0
        val NOT_SENT = "Not sent!"
    }

    // Messenger IPC - Messenger object contains binder to send to client
    private val mMessenger = Messenger(IncomingHandler())

    // Messenger IPC - Message Handler
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // Get message from client. Save recent connected client info.
            val receivedBundle = msg.data
            RecentClient.client = Client(
                receivedBundle.getString(PACKAGE_NAME),
                receivedBundle.getInt(PID).toString(),
                receivedBundle.getString(DATA),
                "Messenger"
            )

            // Send message to the client. The message contains server info
            val message = Message.obtain(this@IncomingHandler, 0)
            val bundle = Bundle()
            bundle.putInt(CONNECTION_COUNT, connectionCount)
            bundle.putInt(PID, android.os.Process.myPid())
            message.data = bundle
            // The service can save the msg.replyTo object as a local variable
            // so that it can send a message to the client at any time
            msg.replyTo.send(message)
        }
    }

    private val binder = object : IMyAidlInterface.Stub() {

        override fun getPid(): Int = android.os.Process.myPid()

        override fun getConnectionCount(): Int = IPCServerService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT
                else data

            RecentClient.client = Client(
                packageName ?: NOT_SENT,
                pid.toString(),
                clientData,
                "AIDL"
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        connectionCount++
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        RecentClient.client = null
        return super.onUnbind(intent)
    }
}
