package net.y1wtt.cuteenginyaer.handler.discord.commands

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationCommandInteractionOption
import discord4j.core.`object`.command.ApplicationCommandInteractionOptionValue
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import reactor.core.publisher.Mono


@Suppress("UNCHECKED_CAST")
class ChatCommand : SlashCommand {
	override val name: String
		get() = "chat"
	override val description: String
		get() = "にゃーん"
	override val options: List<ApplicationCommandOptionData> =
		listOf(
			ApplicationCommandOptionData
				.builder()
				.name("prompt")
				.description("chat prompt")
				.type(ApplicationCommandOption.Type.STRING.value)
				.required(true)
				.build()
		)

	override fun handle(event: ChatInputInteractionEvent): Mono<Unit?> {
		val prompt = event.getOption("prompt")
			.flatMap { obj: ApplicationCommandInteractionOption -> obj.value }
			.map { obj: ApplicationCommandInteractionOptionValue -> obj.asString() }
			.get() //This is warning us that we didn't check if its present, we can ignore this on required options

		return event.reply()
			.withEphemeral(true)
			.withContent("Hello, $prompt") as Mono<Unit?>
	}
}