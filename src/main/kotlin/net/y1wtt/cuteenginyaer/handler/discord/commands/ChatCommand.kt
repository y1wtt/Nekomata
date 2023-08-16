package net.y1wtt.cuteenginyaer.handler.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class ChatCommand : SlashCommand {
	override val name: String
		get() = "chat"
	override val description: String
		get() = "ねこねこAI"
	override val options: List<OptionData> =
		listOf(OptionData(OptionType.STRING,"prompt","質問内容を入れるにゃ！"))

	override fun handle(event: SlashCommandInteractionEvent) {
		event.deferReply().queue()

	}
}