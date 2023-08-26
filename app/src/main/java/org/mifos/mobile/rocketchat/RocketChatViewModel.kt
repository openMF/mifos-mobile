package org.mifos.mobile.rocketchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@HiltViewModel
class RocketChatViewModel @Inject constructor(
) : ViewModel() {
    // will later fetch it from the local storage using the repository
    private val webSocketURL = "wss://testrcmifosworkspace.rocket.chat/websocket"
    private val okHttpClient = OkHttpClient()
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus
    private val _messages = MutableLiveData<Pair<Boolean, String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages


    fun createWebSocketConnection(webSocketListener : WebSocketListener) : WebSocket? {
        return okHttpClient.newWebSocket(createRequest(), webSocketListener)
    }

    fun sendCustomerMessage(webSocket: WebSocket?, message : String) {
        val messageObject = """
        {
            "msg": "method",
            "method": "sendMessage",
            "id": "42",
            "params": [
                {
                    "_id": "${System.currentTimeMillis()}", 
                    "rid": "GENERAL",
                    "msg": "$message"
                }
            ]
        }
    """.trimIndent()
        webSocket?.send(messageObject)
    }

    private fun createRequest(): Request {
        return Request.Builder()
            .url(webSocketURL)
            .build()
    }


    fun addMessage(message: Pair<Boolean, String>) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }
}