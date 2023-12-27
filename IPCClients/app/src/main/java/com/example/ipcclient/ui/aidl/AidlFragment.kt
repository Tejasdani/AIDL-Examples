package com.example.ipcclient.ui.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import android.os.Process
import com.example.ipcclient.R
import com.example.ipcclient.databinding.FragmentAidlBinding
import com.example.ipcserver.IMyAidlInterface

class AidlFragment : Fragment(), ServiceConnection, View.OnClickListener {

    private var _binding: FragmentAidlBinding? = null
    private val binding get() = _binding!!
    private var iRemoteService: IMyAidlInterface? = null
    private var connected = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAidlBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnConnect.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
            connected = if (connected) {
                disconnectToRemoteService()
                binding.tvServerPID.text = ""
                binding.txtServerConnectionCount.text = ""
                binding.btnConnect.text = getString(R.string.connect)
                binding.linearLayoutClientInfo.visibility = View.INVISIBLE
                false
            } else {
                connectToRemoteService()
                binding.linearLayoutClientInfo.visibility = View.VISIBLE
                binding.btnConnect.text = getString(R.string.disconnect)
                true
            }
    }
    private fun connectToRemoteService() {
        val intent = Intent("aidlexample")
        val pack = IMyAidlInterface::class.java.`package`
        println("IPCClient:: ${pack.name}")
        pack?.let {
            intent.setPackage(pack.name)
            activity?.applicationContext?.bindService(
                intent, this, Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun disconnectToRemoteService() {
        if(connected){
            activity?.applicationContext?.unbindService(this)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        // Gets an instance of the AIDL interface named IIPCExample,
        // which we can use to call on the service
        iRemoteService = IMyAidlInterface.Stub.asInterface(service)
        binding.tvServerPID.text = iRemoteService?.pid.toString()
        binding.txtServerConnectionCount.text = iRemoteService?.connectionCount.toString()
        iRemoteService?.setDisplayedValue(
            context?.packageName,
            Process.myPid(),
            binding.edtClientData.text.toString())
        connected = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Toast.makeText(context, "IPC server has disconnected unexpectedly", Toast.LENGTH_LONG).show()
        iRemoteService = null
        connected = false
    }
}
