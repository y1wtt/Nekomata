package net.y1wtt.cuteenginyaer.repository.chatai

interface ChatAI {
	fun completions(context: List<ChatContext>): Result<String>
}