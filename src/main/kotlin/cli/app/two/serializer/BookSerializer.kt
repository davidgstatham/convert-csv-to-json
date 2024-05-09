package cli.app.two.serializer

import cli.app.two.domain.Book

interface BookSerializer {
    fun serializeBooks(books: Collection<Book>): ByteArray
}
