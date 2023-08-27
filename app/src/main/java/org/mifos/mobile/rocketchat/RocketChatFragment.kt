package org.mifos.mobile.rocketchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.mifos.mobile.databinding.FragmentRocketChatBinding
import org.mifos.mobile.rocketchat.adapter.RocketChatAdapter
import org.mifos.mobile.rocketchat.model.SupportChatMessage

class RocketChatFragment : Fragment() {
    private var _binding: FragmentRocketChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RocketChatViewModel
    private var webSocket: WebSocket? = null
    private lateinit var webSocketListener: WebSocketListener
    private lateinit var adapter: RocketChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketChatBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RocketChatViewModel::class.java]
        webSocketListener = RocketChatService(viewModel)
        adapter = RocketChatAdapter()
        binding.rvSupportChat.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel.socketStatus.observe(viewLifecycleOwner) {
                statusTV.text = if (it) "Connected" else "Disconnected"
            }

            viewModel.messages.observe(viewLifecycleOwner) {
                adapter.addNewMessage(it)
            }

            connectButton.setOnClickListener {
                webSocket = viewModel.createWebSocketConnection(webSocketListener)
            }

            disconnectButton.setOnClickListener {
                webSocket?.close(1000, "Canceled manually.")
            }

            sendButton.setOnClickListener {
                viewModel.sendCustomerMessage(webSocket, messageET.text.toString())
                viewModel.addMessage(
                    SupportChatMessage(
                        messageET.text.toString().trim(),
                        SupportChatMessage.MessageType.USER
                    )
                )
                messageET.text = null
            }
        }

    }

}