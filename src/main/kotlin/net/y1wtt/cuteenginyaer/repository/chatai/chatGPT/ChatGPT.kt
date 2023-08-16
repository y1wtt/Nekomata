package net.y1wtt.CuteEnginyaer.repository.chatai.chatGPT

import net.y1wtt.cuteenginyaer.DiscordSubscriber
import net.y1wtt.cuteenginyaer.repository.chatai.ChatAI
import net.y1wtt.cuteenginyaer.repository.chatai.ChatContext
import net.y1wtt.cuteenginyaer.repository.chatai.chatGPT.ChatGPTCompletionRequest
import net.y1wtt.cuteenginyaer.repository.config.AppConfig
import net.y1wtt.cuteenginyaer.repository.config.AppConfigLoader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import reactor.util.Logger
import reactor.util.Loggers


class ChatGPT private constructor(
	appConfig: AppConfig = AppConfigLoader.load(),
	httpClient: OkHttpClient = OkHttpClient()
) : ChatAI {
	private val log: Logger = Loggers.getLogger(DiscordSubscriber::class.java)

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

	override fun completions(context: List<ChatContext>): String? {
		val conf = AppConfigLoader.load().chatgpt
		val body: RequestBody =
			ChatGPTCompletionRequest(conf.modelName, listOf(ChatContext("system", conf.initialPrompt)) + context)
				.toJson()
				.toRequestBody("application/json; charset=utf-8".toMediaType())


		val request: Request = Request.Builder()
			.url(appConfig.chatgpt.chatAIEndpoint)
			.addHeader("Authorization","bearer ${conf.token}")
			.post(body)
			.build()

		try {
			this.httpClient.newCall(request).execute().use { resp ->
				return resp.body?.string();
			}
		} catch (t: Throwable) {
			t.message?.let { log.error(it, t) }
		}
		return null;
	}
}