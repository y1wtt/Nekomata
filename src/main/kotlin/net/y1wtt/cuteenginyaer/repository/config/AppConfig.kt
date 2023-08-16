package net.y1wtt.cuteenginyaer.repository.config

import kotlinx.serialization.Serializable

@Serializable
data class chatgpt(
	val chatAIEndpoint: String = "",
	val token: String = "",
	val model: String = "",
	val initialPrompt: String = ""
)

@Serializable
data class discord(val token: String = "")

@Serializable
data class AppConfig(
	val chatgpt: chatgpt,
	val discord: discord
)