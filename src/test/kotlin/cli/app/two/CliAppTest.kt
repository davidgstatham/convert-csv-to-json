package cli.app.two

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CliAppTest {
    @Test
    fun testWithCommandLineOption() {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))

        val args = arrayOf("src/test/resources/input.csv", "src/test/resources/output.json")
        PicocliRunner.run(CliApp::class.java, ctx, *args)

        Assertions.assertTrue(baos.toString().contains("Parsed 3 books"))
        Assertions.assertTrue(baos.toString().contains("Successfully written books to "))

        ctx.close()
    }
}
