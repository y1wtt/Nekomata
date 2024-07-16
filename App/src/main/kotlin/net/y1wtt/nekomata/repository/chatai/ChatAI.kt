package net.y1wtt.nekomata.repository.chatai

import net.y1wtt.nekomata.entities.chatai.ChatContext

fun interface ChatAI<T> {
	fun completions(context: List<ChatContext>): Result<T>
}