package repository.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
	val chatAIEndpoint: String = "",
	val initialPrompt: String = ""
)