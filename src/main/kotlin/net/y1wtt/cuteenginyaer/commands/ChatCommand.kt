package net.y1wtt.cuteenginyaer.commands

import com.fasterxml.jackson.databind.ObjectMapper
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT.ChatGPT

class ChatCommand : SlashCommand {
	override val name: String
		get() = "chat"
	override val description: String
		get() = "ねこねこAI"
	override val options: List<OptionData> =
		listOf(OptionData(OptionType.STRING, "prompt", "質問内容を入れるにゃ！"))

	override fun handle(event: SlashCommandInteractionEvent) {
		event.deferReply().queue()
		event.hook.sendMessageEmbeds(
			EmbedBuilder()
				.setTitle("${event.user.effectiveName} からの質問にゃ！")
				.addField(event.user.name, "${event.getOption("prompt")?.asString}", false)
				.build()
		).queue { m ->
			m?.createThreadChannel(event.getOption("prompt")?.asString)?.queue {
				//TODO DIとかでいい感じにしたいね
				ChatGPT.getInstance().completions(listOf())?.let { response ->
					it?.sendMessage(
						//TODO エラー処理書きたいね
						ObjectMapper()
							.readTree(response)
							.get("choices")
							.get(0)
							.get("message")
							.get("content")
							.toString()
					)?.queue()
				}
			}
		}

	}
}