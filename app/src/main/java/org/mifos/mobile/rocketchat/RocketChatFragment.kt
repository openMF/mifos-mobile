package org.mifos.mobile.rocketchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRocketChatBinding

class RocketChatFragment : Fragment() {
    private var _binding : FragmentRocketChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : RocketChatViewModel
    private var webSocket: WebSocket? = null
    private lateinit var webSocketListener: WebSocketListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketChatBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RocketChatViewModel::class.java]
        webSocketListener = RocketChatService(viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messageET = binding.messageET
        val sendMessageButton  = binding.sendButton
        val connectButton = binding.connectButton
        val disconnectButton = binding.disconnectButton
        val statusTV = binding.statusTV
        val messageTV =  binding.messageTV

        viewModel.socketStatus.observe(viewLifecycleOwner) {
            statusTV.text = if (it) "Connected" else "Disconnected"
        }

        var text = ""
        viewModel.messages.observe(viewLifecycleOwner) {
            text += "${if (it.first) "You: " else "Other: "} ${it.second}\n"

            messageTV.text = text
        }

        connectButton.setOnClickListener {
            webSocket = viewModel.createWebSocketConnection(webSocketListener)
        }

        disconnectButton.setOnClickListener {
            webSocket?.close(1000, "Canceled manually.")
        }

        sendMessageButton.setOnClickListener {
            viewModel.sendCustomerMessage(webSocket, messageET.text.toString())
            viewModel.addMessage(Pair(true, messageET.text.toString()))
            messageET.text = null
        }

    }

}