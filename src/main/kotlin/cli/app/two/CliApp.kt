package cli.app.two

import cli.app.two.deserialiser.BookDeserializer
import cli.app.two.deserialiser.BookParseResult
import cli.app.two.serializer.BookSerializer
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File

@Command(
    name = "convert",
    description = ["..."],
    mixinStandardHelpOptions = true,
)
class CliApp : Runnable {
    @Inject
    private lateinit var bookDeserializer: BookDeserializer

    @Inject
    private lateinit var bookSerializer: BookSerializer

    @Parameters(index = "0", description = ["The input CSV file"])
    private lateinit var inputFile: File

    @Parameters(index = "1", description = ["The output Json file"])
    private lateinit var outputFile: File

    override fun run() {
        runBlocking {
            when (val parseResult = bookDeserializer.deserialize(inputFile.inputStream())) {
                is BookParseResult.Failure -> println("Error parsing books: ${parseResult.message}")
                is BookParseResult.Success -> {
                    println("Parsed ${parseResult.books.size} books..")
                    val serializedBook = bookSerializer.serializeBooks(parseResult.books)
                    outputFile.outputStream().write(serializedBook)
                    println("Successfully written books to ${outputFile.path}")
                }
            }
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.run(CliApp::class.java, *args)
        }
    }
}
