package net.y1wtt.cuteenginyaer.repository.config

import com.charleskorn.kaml.Yaml
import net.y1wtt.cuteenginyaer.model.config.AppConfig

object AppConfigLoader {

	private val yamlLoader = Yaml.default
	var instance: AppConfig? = null

	fun load(): AppConfig {
		if (instance == null) {
			val inputStream = this::class.java.classLoader.getResourceAsStream("AppConf.yml")
			val text = inputStream?.bufferedReader().use { it?.readText()!! }
			instance = yamlLoader.decodeFromString(AppConfig.serializer(), text ?: "")
		}
		return instance ?: throw RuntimeException("fail to load AppConfig")
	}
}