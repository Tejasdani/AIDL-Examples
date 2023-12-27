package com.example.ipcserver

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ipcserver.data.RecentClient
import com.example.ipcserver.databinding.ActivityMainBinding
import com.example.ipcserver.ui.theme.IPCServerTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val client = RecentClient.client
        binding.tvConnectionStatus.text =
            if (client == null) {
                binding.layClientState.visibility = View.GONE
                getString(R.string.no_connected_client)
            } else {
                binding.layClientState.visibility = View.VISIBLE
                getString(R.string.last_connected_client_info)
            }
        println("IPCServer:: ${client?.clientData}")
        println("IPCServer:: ${client?.clientPackageName}")
        println("IPCServer:: ${client?.ipcMethod}")
        binding.tvPkgName.text = client?.clientPackageName
        binding.txtServerPid.text = client?.clientProcessId
        binding.tvData.text = client?.clientData
        binding.tvIPCValue.text = client?.ipcMethod
    }
}