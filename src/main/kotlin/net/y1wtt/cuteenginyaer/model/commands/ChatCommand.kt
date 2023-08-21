package net.y1wtt.cuteenginyaer.model.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT.ChatGPT
import net.y1wtt.cuteenginyaer.model.chatai.ChatContext
import net.y1wtt.cuteenginyaer.repository.discord.ThreadRepository

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
					ThreadRepository(it).insertByResult(response)
				}
			}
		}

	}
}