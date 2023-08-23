package net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import net.y1wtt.nekomata.entities.chatai.ChatContext
import net.y1wtt.nekomata.entities.config.AppConfig
import net.y1wtt.nekomata.repository.chatai.ChatAI
import net.y1wtt.nekomata.repository.chatai.chatGPT.ChatGPTCompletionRequest
import net.y1wtt.nekomata.repository.config.AppConfigLoader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


class ChatGPT private constructor(
	appConfig: AppConfig = AppConfigLoader.load(),
	httpClient: OkHttpClient = OkHttpClient.Builder()
		.readTimeout(1, TimeUnit.MINUTES)
		.build()
) : ChatAI<String> {
	private val log = LoggerFactory.getLogger(this.javaClass)

	private val appConfig: AppConfig;
	private val httpClient: OkHttpClient;

	init {
		instance = this
		this.appConfig = appConfig
		this.httpClient = httpClient
	}

	companion object {
		private var instance: ChatGPT? = null


		fun getInstance(): ChatGPT {
			return instance ?: ChatGPT();
		}
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
			this.httpClient.newCall(request).execute().use { resp ->
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