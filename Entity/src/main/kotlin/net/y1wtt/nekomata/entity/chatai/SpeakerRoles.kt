 package net.y1wtt.nekomata.entity.chatai

enum class SpeakerRoles(val role: String) {
    SYSTEM("system"),
    ASSIST("assist"),
    USER("user");
    override fun toString(): String {
        return role
    }
}