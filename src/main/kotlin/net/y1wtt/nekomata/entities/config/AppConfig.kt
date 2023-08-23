package net.y1wtt.nekomata.entities.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatGPT(
	val chatAIEndpoint: String = "",
	val token: String = "",
	val model: String = "",
	val initialPrompt: String = ""
)

@Serializable
data class Discord(val token: String = "")

@Serializable
data class AppConfig(
	@SerialName("chatgpt")
	val chatGPT: ChatGPT,
	val discord: Discord
)