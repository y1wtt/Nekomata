package net.y1wtt.cuteenginyaer

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.y1wtt.cuteenginyaer.handler.discord.CommandListener
import net.y1wtt.cuteenginyaer.repository.config.AppConfigLoader
import reactor.util.Logger
import reactor.util.Loggers


class DiscordSubscriber {
	private val log: Logger = Loggers.getLogger(DiscordSubscriber::class.java)

	companion object {
		val client: JDA = JDABuilder.createLight(AppConfigLoader.load().discord.token)
			.addEventListeners(CommandListener())
			.build()
	}

	init {
		client.updateCommands().addCommands(
			CommandListener().commandList()
		).queue()
	}
}