package net.y1wtt.nekomata.service.chatAI

import net.y1wtt.nekomata.entity.chatai.ChatContext
import kotlin.Result;
import net.y1wtt.nekomata.entity.chatai.ChatEntry;

interface ChatAIRepository<T> {
    fun completions(context: ChatContext): Result<T>
}