package net.y1wtt.cuteenginyaer.handler.discord.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ApplicationCommandOptionData

import reactor.core.publisher.Mono


interface SlashCommand {
	val name: String
	val description:String
	val options:List<ApplicationCommandOptionData>
	fun handle(event: ChatInputInteractionEvent): Mono<Unit?>
}