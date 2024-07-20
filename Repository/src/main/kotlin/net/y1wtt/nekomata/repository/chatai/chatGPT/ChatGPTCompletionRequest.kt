package net.y1wtt.nekomata.repository.chatai.chatGPT

import com.fasterxml.jackson.databind.ObjectMapper
import net.y1wtt.nekomata.entity.chatai.ChatContext
import net.y1wtt.nekomata.entity.chatai.SpeakerRoles

data class ChatGPTChatEntry(val role: String, val content: String)

data class ChatGPTCompletionRequest(
    val model: String,
    val messages: List<ChatGPTChatEntry>
) {
    constructor(model: String, messages: ChatContext) : this(model, messages.ctx.map {
        ChatGPTChatEntry(
            when (it.role) {
                SpeakerRoles.SYSTEM -> "system"
                SpeakerRoles.ASSISTANT -> "assistant"
                SpeakerRoles.USER -> "user"
            },
            it.content
        )
    })

    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}