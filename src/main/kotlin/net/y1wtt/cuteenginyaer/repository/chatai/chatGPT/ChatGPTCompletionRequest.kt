package net.y1wtt.cuteenginyaer.repository.chatai.chatGPT

import com.fasterxml.jackson.databind.ObjectMapper
import net.y1wtt.cuteenginyaer.model.chatai.ChatContext

data class ChatGPTCompletionRequest(
	val model: String = "",
	val messages: List<ChatContext>
) {
	fun toJson(): String {
		return ObjectMapper().writeValueAsString(ChatGPTCompletionRequest(model, messages))
	}
}