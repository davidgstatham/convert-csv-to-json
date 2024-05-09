package cli.app.two.serializer

import cli.app.two.domain.Book
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Singleton
class JsonBookSerializer(private val objectMapper: ObjectMapper) : BookSerializer {
    override fun serializeBooks(books: Collection<Book>): ByteArray {
        return objectMapper.writeValueAsBytes(books)
    }
}

@Factory
class JacksonFactory {
    @Singleton
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}
