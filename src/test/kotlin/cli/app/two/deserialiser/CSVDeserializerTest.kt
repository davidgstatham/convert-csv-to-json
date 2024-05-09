package cli.app.two.deserialiser

import cli.app.two.domain.Book
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CSVDeserializerTest{

    private lateinit var csvDeserializer: CSVDeserializer

    @BeforeEach
    fun beforeEach() {
        csvDeserializer = CSVDeserializer()
    }

    @Test
    fun `Returns parseResult failure on unexpected input`(){
        runBlocking {
            val invalidInput = """
            Title,Author,ISBN,My Rating,Average Rating,Publisher,Binding,Year Published,Original Publication Year,Date Read,Date Added,Bookshelves,My Review
            Accelerate,
            """.trimMargin()
            val result = csvDeserializer.deserialize(invalidInput.byteInputStream())
            result.shouldBeInstanceOf<BookParseResult.Failure>()
        }
    }

    @Test
    fun `Parsing header CSV returns zero books`(){
        runBlocking {
            val input = """Title,Author,ISBN,My Rating,Average Rating,Publisher,Binding,Year Published,Original Publication Year,Date Read,Date Added,Bookshelves,My Review"""
            val result = csvDeserializer.deserialize(input.byteInputStream())
            result.shouldBeInstanceOf<BookParseResult.Success>()
            result.books shouldHaveSize  0
        }
    }

    @Test
    fun `Parsing empty CSV returns 0 books`(){
        runBlocking {
            val input = ""
            val result = csvDeserializer.deserialize(input.byteInputStream())
            result.shouldBeInstanceOf<BookParseResult.Success>()
            result.books shouldHaveSize 0
        }
    }

    @Test
    fun `Can parse valid input to flows`(){
        val validInput = """
            Title,Author,ISBN,My Rating,Average Rating,Publisher,Binding,Year Published,Original Publication Year,Date Read,Date Added,Bookshelves,My Review
            Accelerate," Nicole Forsgren, Jez Humble, Gene Kim", 9781942788331, 5, 4.09, IT Revolution, paperback, 2018, 2018, 2018-06-02, 2018-03-29, ,
            Microservices Patterns, Chris Richardson, 9781617294549, 4, 4.15, Manning Publications, Paperback, 2017, 2017, , 2019-09-19, ,
            Better Allies, Karen Catlin, 9781732723313, 5, 4.33, Better Allies Press, Paperback, 2019, 2019, 2020-01-15, 2020-10-19, ,
        """.trimIndent()

        runBlocking {
            val result = csvDeserializer.deserialize(validInput.byteInputStream())
            result.shouldBeInstanceOf<BookParseResult.Success>()
            result.books shouldHaveSize 3
            result.books shouldContainInOrder listOf(
                Book(title = "Accelerate", author = "Nicole Forsgren, Jez Humble, Gene Kim", isbn = "9781942788331"),
                Book(title = "Microservices Patterns", author = "Chris Richardson", isbn = "9781617294549"),
                Book(title = "Better Allies", author = "Karen Catlin", isbn = "9781732723313")
            )
        }
    }

}
