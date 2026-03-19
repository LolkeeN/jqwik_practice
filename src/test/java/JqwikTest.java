import net.jqwik.api.*;
import net.jqwik.api.arbitraries.BigDecimalArbitrary;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.arbitraries.StringArbitrary;
import org.example.Author;
import org.example.Book;
import org.example.BookStore;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JqwikTest {

    @Property(tries = 1_000)
    void testBookGeneration(@ForAll("generateBook") Book book) {
        System.out.println(book);
        assertEquals(book.isBig(), book.getPages() >= 600);
        assertEquals(book.getAuthor().isOld(), book.getAuthor().getAge() >= 60);
    }

    @Property(tries = 1_000)
    void testBookStore(@ForAll("generateBookStores") BookStore bookStore) {
        System.out.println(bookStore);

        Book book = generateBook().sample();
        Author author = generateAuthor().sample();
        bookStore.addBook(author, book);

        assertTrue(bookStore.containsBookByAuthor(book, author));
        assertTrue(bookStore.hasAuthor(author));
        assertTrue(bookStore.hasAuthor(author.getName()));

        assertFalse(bookStore.hasAuthor(UUID.randomUUID().toString()));

        bookStore.removeBook(author.getName(), book);

        assertFalse(bookStore.containsBookByAuthor(book, author));
        assertTrue(bookStore.hasAuthor(author));
        assertTrue(bookStore.hasAuthor(author.getName()));

        bookStore.removeAuthor(author);
        assertFalse(bookStore.hasAuthor(author));
        assertFalse(bookStore.hasAuthor(author.getName()));
    }

    @Provide
    private Arbitrary<Author> generateAuthor() {
        StringArbitrary authorName = Arbitraries.strings().withCharRange('a', 'z');
        BigDecimalArbitrary authorWealth = Arbitraries.bigDecimals();
        IntegerArbitrary authorAge = Arbitraries.integers().between(0, 100);

        return Combinators.combine(authorName.injectDuplicates(0), authorAge, authorWealth).as(Author::new);
    }

    @Provide
    Arbitrary<Book> generateBook() {
        StringArbitrary bookName = Arbitraries.strings();
        Arbitrary<Author> authorArbitrary = generateAuthor();
        IntegerArbitrary bookPages = Arbitraries.integers().greaterOrEqual(1);

        return Combinators.combine(bookName, authorArbitrary, bookPages).as(Book::new);
    }

    @Provide
    Arbitrary<Set<Book>> generateBooks() {
        return generateBook().set();
    }

    @Provide
    Arbitrary<BookStore> generateBookStores() {
        return generateBook().list().ofMinSize(0).ofMaxSize(20)
                .map( books -> {
                   BookStore bookStore = new BookStore();
                   books.forEach(book -> bookStore.addBook(book.getAuthor(), book));

                   return bookStore;
                });
    }
}
