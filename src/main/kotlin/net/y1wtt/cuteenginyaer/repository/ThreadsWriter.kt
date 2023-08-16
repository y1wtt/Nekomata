package net.y1wtt.cuteenginyaer.repository

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import java.awt.Color

class ThreadsWriter(tc:ThreadChannel?) {
	private val channel:ThreadChannel?
	init {
		channel = tc
	}

	fun writeByResult(res:Result<String>): Unit {
		if (res.isSuccess) {
			channel?.sendMessage(res.getOrDefault(""))?.queue()
		} else {
			println(res.exceptionOrNull()?.message?:"null")
			channel?.sendMessageEmbeds(
				EmbedBuilder()
					.setTitle("Error has occurred")
					.addField("ERR", res.exceptionOrNull()?.message?:"null", false)
					.setColor(Color.RED)
					.build()
			)?.queue()
		}
	}
}