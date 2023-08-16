package net.y1wtt.cuteenginyaer.commands

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT.ChatGPT
import net.y1wtt.cuteenginyaer.repository.chatai.ChatContext
import java.awt.Color

class ChatCommand : SlashCommand {
	override val name: String
		get() = "chat"
	override val description: String
		get() = "ねこねこAI"
	override val options: List<OptionData> =
		listOf(OptionData(OptionType.STRING, "prompt", "質問内容を入れるにゃ！").setRequired(true))

	override fun handle(event: SlashCommandInteractionEvent) {
		event.deferReply().queue()
		val prompt = event.getOption("prompt")!!.asString;
		event.hook.sendMessageEmbeds(
			EmbedBuilder()
				.setTitle("${event.user.effectiveName} からの質問にゃ！")
				.addField(event.user.name, prompt, false)
				.build()
		).queue { m ->
			m?.createThreadChannel(prompt)?.queue {
				it.sendTyping().queue()
				//TODO DIとかでいい感じにしたいね
				ChatGPT.getInstance().completions(listOf(ChatContext("user", prompt)))?.let { response ->
					println(response)
					//TODO エラー処理書きたいね
					// パース処理を関心分けたい
					var res = ObjectMapper()
						.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
						.readTree(response)
						.path("choices")
						.path(0)
						.path("message")
						.path("content")
						.toString()
					if (res.isBlank()) {
						res = ObjectMapper()
							.readTree(response)
							.path("error")
							.path("message")
							.toString()
						if (res.isBlank()) {
							res = response
						}
						it?.sendMessageEmbeds(
							EmbedBuilder()
								.setTitle("Error has occurred")
								.addField("ERR", res, false)
								.setColor(Color.RED)
								.build()
						)?.queue()
					} else {
						it?.sendMessage(
							res.trim()
						)?.queue()
					}
				}
			}
		}

	}
}