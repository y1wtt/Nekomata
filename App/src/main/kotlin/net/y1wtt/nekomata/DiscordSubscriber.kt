package net.y1wtt.nekomata

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.y1wtt.nekomata.handler.discord.CommandListener
import net.y1wtt.nekomata.repository.config.AppConfigLoader
import org.slf4j.LoggerFactory


class DiscordSubscriber {
	private val log = LoggerFactory.getLogger(DiscordSubscriber::class.java)

	companion object {
		val client: JDA = JDABuilder.createLight(
			AppConfigLoader.load().discord.token,
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.MESSAGE_CONTENT
		)
			.addEventListeners(CommandListener())
			.build()
	}

	init {
		log.debug("init")
		client.updateCommands().addCommands(
			CommandListener().commandList()
		).queue()
	}
}