package cli.app.two.serializer

import cli.app.two.domain.Book
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.assertions.json.shouldEqualJson
import org.junit.jupiter.api.Test

class JsonBookSerializerTest {
    @Test
    fun `can serialize books to json`() {
        val jsonSerializer = JsonBookSerializer(ObjectMapper())
        val books =
            listOf(
                Book("title1", "author1", "isbn1"),
                Book("title2", "author2", "isbn2"),
                Book("title3", "author3", "isbn3"),
            )

        val json = jsonSerializer.serializeBooks(books).toString(Charsets.UTF_8)
        json.shouldBeValidJson()
        json shouldEqualJson
            """
            [
                {"title":"title1","author":"author1","isbn":"isbn1"},
                {"title":"title2","author":"author2","isbn":"isbn2"},
                {"title":"title3","author":"author3","isbn":"isbn3"}
            ]
            """.trimIndent()
    }
}
