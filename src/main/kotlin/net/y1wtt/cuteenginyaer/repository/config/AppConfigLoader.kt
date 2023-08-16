package net.y1wtt.cuteenginyaer.repository.config

import com.charleskorn.kaml.Yaml

object AppConfigLoader {
	private val yamlLoader = Yaml.default
	private var text = ""
	fun load(): AppConfig {
		if (text.length == 0){
			val inputStream = this::class.java.classLoader.getResourceAsStream("AppConf.yml")
			text = inputStream?.bufferedReader().use { it?.readText()!! }
		}
		return yamlLoader.decodeFromString(AppConfig.serializer(), text ?: "")
	}
}