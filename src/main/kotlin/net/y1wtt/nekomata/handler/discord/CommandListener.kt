package net.y1wtt.nekomata.handler.discord

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT.ChatGPT
import net.y1wtt.nekomata.entities.chatai.ChatContext
import net.y1wtt.nekomata.entities.commands.ChatCommand
import net.y1wtt.nekomata.entities.commands.SlashCommand
import net.y1wtt.nekomata.repository.discord.ThreadRepository
import org.slf4j.LoggerFactory


class CommandListener : ListenerAdapter() {

	private val log = LoggerFactory.getLogger(this.javaClass)
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
		this.handleCommands(e)
	}

	override fun onGenericEvent(event: GenericEvent) {
		log.debug("{}", event)
	}

	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.message.channelType == ChannelType.GUILD_PUBLIC_THREAD) {
			val history = event.message.channel.asGuildMessageChannel().iterableHistory
			val subList = history.sortedBy { message: Message? -> message?.timeCreated }
				.subList(1, history.count())
			if (subList.isNotEmpty() && subList.last()?.author?.isBot == false) {
				val channel = event.message.channel.asThreadChannel()
				channel.sendTyping().queue()
				//TODO DIとかでいい感じにしたいね
				ChatGPT.getInstance().completions(subList.map {
					ChatContext(if (it.author.isBot) "assistant" else "user", it.contentRaw)
				}).let {
					ThreadRepository(channel).insertByResult(it)
				}

			}
		}
	}

	fun handleCommands(event: SlashCommandInteractionEvent) {

		commands.filter { command: SlashCommand ->
			command.name == event.name
		}.forEach() {
			it.handle(event)
		}
	}
}