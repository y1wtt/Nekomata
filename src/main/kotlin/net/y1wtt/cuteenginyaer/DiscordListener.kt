package net.y1wtt.cuteenginyaer

import discord4j.core.DiscordClientBuilder
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.guild.GuildCreateEvent
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.discordjson.json.ApplicationCommandData
import discord4j.rest.interaction.GuildCommandRegistrar
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import net.y1wtt.cuteenginyaer.handler.discord.CommandLitener
import net.y1wtt.cuteenginyaer.repository.config.AppConfigLoader
import reactor.core.publisher.Mono
import reactor.util.Logger
import reactor.util.Loggers


class DiscordListener {
	private val log: Logger = Loggers.getLogger(DiscordListener::class.java)

	companion object {
		val client = DiscordClientBuilder.create(AppConfigLoader.load().discord.token)
			.build()
	}

	init {
		client.gateway()
			.withGateway {
				mono {
					it.on(ReadyEvent::class.java).subscribe { rdy ->
						log.debug("{}", rdy)
					}
					it.on(GuildCreateEvent::class.java).subscribe {
						GuildCommandRegistrar.create(
							it.client.rest(),
							CommandLitener().commandList()
						)
							.registerCommands(it.guild.id)
							.doOnError { e: Throwable? -> log.warn("Unable to create guild command", e) }
							.onErrorResume { _: Throwable? -> Mono.empty<ApplicationCommandData?>() }
							.blockLast()
					}
					it.on(Event::class.java).subscribe {
						log.warn("{}", it)
					}
					it.on(ChatInputInteractionEvent::class.java).subscribe {
						CommandLitener().handle(it)?.block()
					}
				}
			}
			.block()
	}
}