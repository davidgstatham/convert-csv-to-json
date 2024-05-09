package cli.app.two.deserialiser

import cli.app.two.domain.Book
import java.io.InputStream

interface BookDeserializer {
    suspend fun deserialize(inputStream: InputStream): BookParseResult
}

sealed class BookParseResult{
    data class Success(val books: List<Book>): BookParseResult()
    data class Failure(val message: String): BookParseResult()
}
