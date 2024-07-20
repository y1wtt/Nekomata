package net.y1wtt.nekomata.entity.chatai

data class ChatEntry(
    val role: SpeakerRoles,
    val content: String
)

data class ChatContext(val ctx: List<ChatEntry>)