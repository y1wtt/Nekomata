package net.y1wtt.nekomata.service.chatAI

import kotlin.Result;
import net.y1wtt.nekomata.entity.chatai.ChatContext;

interface ChatAIRepository<T> {
    fun completions(context: List<ChatContext>): Result<T>
}