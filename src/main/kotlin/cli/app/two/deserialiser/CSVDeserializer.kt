package cli.app.two.deserialiser

import cli.app.two.domain.Book
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.util.MalformedCSVException
import jakarta.inject.Singleton
import java.io.InputStream

@Singleton
class CSVDeserializer : BookDeserializer {
    companion object {
        private const val TITLE_FILED = "Title"
        private const val AUTHOR_FILED = "Author"
        private const val ISBN_FIELD = "ISBN"
    }

    override suspend fun deserialize(inputStream: InputStream): BookParseResult {
        try {
            val books =
                csvReader().openAsync(inputStream) {
                    readAllWithHeaderAsSequence().map { row ->
                        parseRowToBook(row)
                    }.toList()
                }
            return BookParseResult.Success(books)
        } catch (e: Exception) {
            return when (e) {
                is IllegalArgumentException -> BookParseResult.Failure(message = "Failed to parse input CSV: ${e.message}")
                is MalformedCSVException -> BookParseResult.Failure(message = "Malformed input CSV: ${e.message}")
                else -> throw e
            }
        }
    }

    private fun parseRowToBook(row: Map<String, String>): Book {
        return if (!row.contains(TITLE_FILED) && row.contains(AUTHOR_FILED) && row.contains(ISBN_FIELD)) {
            throw IllegalArgumentException("Missing required fields in row: $row")
        } else {
            Book(
                title = row[TITLE_FILED]!!.trim(),
                author = row[AUTHOR_FILED]!!.trim(),
                isbn = row[ISBN_FIELD]!!.trim(),
            )
        }
    }
}
