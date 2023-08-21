package net.y1wtt.cuteenginyaer.repository.chatai

import net.y1wtt.cuteenginyaer.model.chatai.ChatContext

fun interface ChatAI<T> {
	fun completions(context: List<ChatContext>): Result<T>
}