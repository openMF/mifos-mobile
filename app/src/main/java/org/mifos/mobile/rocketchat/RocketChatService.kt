package org.mifos.mobile.rocketchat

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject

class RocketChatService @Inject constructor(
    private val viewModel: RocketChatViewModel
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
        val connectMessage = """
                    {
                        "msg": "connect",
                        "version": "1",
                        "support": ["1"]
                    }
                """.trimIndent()
        webSocket.send(connectMessage)

        val loginRequest = """
                    {
                        "msg": "method",
                        "method": "login",
                        "id": "42",
                        "params": [
                            { "resume": "EDFOiOPiwS5k-ptDA9-bDQ7qM2DVXiDDh-5YrzrLfxA" }
                        ]
                    }
                """.trimIndent()
        webSocket.send(loginRequest)

        val subscribeRequest = """
                    {
                        "msg": "sub",
                        "id": "sub1",
                        "name": "stream-room-messages",
                        "params": ["GENERAL", false]
                    }
                """.trimIndent()
        webSocket.send(subscribeRequest)

//        Log.d(TAG, "onOpen:")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        // only call viewModel.addMessage(pair(false, text)) if the string text is of the type response I shared.

        if (text.contains("\"msg\":\"changed\"") && text.contains("\"collection\":\"stream-room-messages\"") && !text.contains("\"username\":\"CustomerTestMifosRCWorkspace\"")) {
            val jsonObject = JSONObject(text)
            val fieldsObject = jsonObject.getJSONObject("fields")
            val argsArray = fieldsObject.getJSONArray("args")
            val messageObject = argsArray.getJSONObject(0)
            val message = messageObject.getString("msg")

            viewModel.addMessage(Pair(false, message))
        }

        if (text.contains("\"msg\":\"ping\"")) {
            val pongMessage = """
                        {
                            "msg": "pong"
                        }
                    """.trimIndent()
            webSocket.send(pongMessage)
        }
//        Log.d(TAG, "onMessage: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
//        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(false)
//        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}