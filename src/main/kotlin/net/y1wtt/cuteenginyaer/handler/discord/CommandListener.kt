package net.y1wtt.cuteenginyaer.handler.discord

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.y1wtt.cuteenginyaer.handler.discord.commands.ChatCommand
import net.y1wtt.cuteenginyaer.handler.discord.commands.SlashCommand


class CommandListener : ListenerAdapter() {

	private val commands: MutableList<SlashCommand> = ArrayList()

	init {
		commands.add(ChatCommand())
	}

	fun commandList(): List<CommandData> {
		return commands.map {
			Commands.slash(it.name, it.description).addOptions(
				it.options
			)
		}
	}

	override fun onSlashCommandInteraction(e: SlashCommandInteractionEvent) {
		println(e)
		this.handle(e)
	}

	override fun onMessageReceived(event: MessageReceivedEvent) {
		println(event)
	}

	fun handle(event: SlashCommandInteractionEvent) {

		commands.filter { command: SlashCommand ->
			command.name == event.name
		}.forEach() {
			it.handle(event)
		}
	}
}