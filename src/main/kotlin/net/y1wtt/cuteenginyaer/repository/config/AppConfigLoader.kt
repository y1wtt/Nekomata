package net.y1wtt.cuteenginyaer.repository.config

import com.charleskorn.kaml.Yaml

object AppConfigLoader {
	private val yamlLoader = Yaml.default
	private var text = ""
	fun load(): AppConfig {
		if (text.isEmpty()){
			val inputStream = this::class.java.classLoader.getResourceAsStream("AppConf.yml")
			text = inputStream?.bufferedReader().use { it?.readText()!! }
		}
		return yamlLoader.decodeFromString(AppConfig.serializer(), text ?: "")
	}
}