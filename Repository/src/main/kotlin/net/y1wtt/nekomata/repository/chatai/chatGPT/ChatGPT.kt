 package net.y1wtt.nekomata.repository.chatai.chatGPT

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import net.y1wtt.nekomata.entity.chatai.ChatContext
import net.y1wtt.nekomata.entity.config.AppConfig
import net.y1wtt.nekomata.repository.config.AppConfigLoader
import net.y1wtt.nekomata.service.chatAI.ChatAI
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


class ChatGPT(
    private val appConfig: AppConfig,
) : ChatAI<String> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    override fun completions(context: List<ChatContext>): Result<String> {
        val conf = AppConfigLoader.load().chatGPT
        val ctx = listOf(ChatContext("system", conf.initialPrompt)) + context
        val body: RequestBody =
            ChatGPTCompletionRequest(conf.model, ctx)
                .toJson()
                .toRequestBody("application/json; charset=utf-8".toMediaType())
        val request: Request = Request.Builder()
            .url(appConfig.chatGPT.chatAIEndpoint)
            .addHeader("Authorization", "Bearer ${conf.token}")
            .post(body)
            .build()

        try {
            httpClient.newCall(request).execute().use { resp ->
                val body = resp.body?.string()
                var json = ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                    .readTree(body)
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .textValue()
                if (json.isNotBlank()) {
                    return Result.success(json);
                }

                json = ObjectMapper()
                    .readTree(body)
                    .path("error")
                    .path("message")
                    .textValue()
                return Result.failure(IOException(json.ifBlank { body }))
            }
        } catch (t: Throwable) {
            t.message?.let { log.error(it, t) }
            return Result.failure(t)
        }
    }
}