package net.y1wtt.cuteenginyaer.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData


interface SlashCommand {
	val name: String
	val description:String
	val options:List<OptionData>
	fun handle(event: SlashCommandInteractionEvent)
}