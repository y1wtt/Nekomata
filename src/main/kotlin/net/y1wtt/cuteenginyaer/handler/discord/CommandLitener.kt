package net.y1wtt.cuteenginyaer.handler.discord

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ApplicationCommandRequest
import net.y1wtt.cuteenginyaer.handler.discord.commands.ChatCommand
import net.y1wtt.cuteenginyaer.handler.discord.commands.SlashCommand
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class CommandLitener {

	private val commands: MutableList<SlashCommand> = ArrayList()

	init {
		commands.add(ChatCommand())
	}

	fun commandList(): List<ApplicationCommandRequest> {
		return commands.map {
			ApplicationCommandRequest.builder()
				.name(it.name ?: "")
				.description(it.description ?: "")
				.addAllOptions(
					it.options
				).build()
		}
	}
	fun handle(event: ChatInputInteractionEvent): Mono<Unit>? {
		// Convert our array list to a flux that we can iterate through
		return Flux.fromIterable<SlashCommand>(commands) //Filter out all commands that don't match the name of the command this event is for
			.filter { command: SlashCommand -> command.name == event.commandName } // Get the first (and only) item in the flux that matches our filter
			.next() //have our command class handle all the logic related to its specific command.
			.flatMap { command: SlashCommand -> command.handle(event) }
	}
}