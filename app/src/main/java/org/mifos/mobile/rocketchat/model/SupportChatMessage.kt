package org.mifos.mobile.rocketchat.model

data class SupportChatMessage(
        val text : String,
        val messageType : MessageType
) {
    enum class MessageType{
        USER, SUPPORT
    }
}
