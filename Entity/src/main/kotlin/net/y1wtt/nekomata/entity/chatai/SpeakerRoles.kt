 package net.y1wtt.nekomata.entity.chatai

enum class SpeakerRoles(private val role: String) {
    SYSTEM("system"),
    ASSISTANT("assistant"),
    USER("user");
    override fun toString(): String {
        return role
    }
}