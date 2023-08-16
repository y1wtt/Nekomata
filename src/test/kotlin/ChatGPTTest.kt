import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import net.y1wtt.cuteenginyaer.repository.chatai.ChatContext
import net.y1wtt.cuteenginyaer.repository.chatai.chatGPT.ChatGPTCompletionRequest

class ChatGPTTest {
	@Test
	fun testRequestBody() {
		Assertions.assertEquals(
			"""{"model":"gpt-3.5-turbo","messages":[{"role":"user","content":"What is the OpenAI mission?"}]}""",
			ChatGPTCompletionRequest("gpt-3.5-turbo", listOf(ChatContext("user","What is the OpenAI mission?"))).toJson()
		)
	}
}