package repository.config

import com.charleskorn.kaml.Yaml

object AppConfigLoader {
	private val yamlLoader = Yaml.default
	fun load(): AppConfig {
		val inputStream = this::class.java.classLoader.getResourceAsStream("AppConf.yml")
		val text = inputStream?.bufferedReader().use { it?.readText() }
		return yamlLoader.decodeFromString(AppConfig.serializer(), text ?: "")
	}
}