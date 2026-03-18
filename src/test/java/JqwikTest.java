import net.jqwik.api.*;
import net.jqwik.api.arbitraries.BigDecimalArbitrary;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.arbitraries.StringArbitrary;
import org.example.Author;
import org.example.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JqwikTest {

    @Property(tries = 1_000)
    void testBookGeneration(@ForAll("generateBooks") Book book) {
        assertEquals(book.isBig(), book.getPages() >= 600);
        assertEquals(book.getAuthor().isOld(), book.getAuthor().getAge() >= 60);
    }

    @Provide
    private Arbitrary<Author> generateAuthors() {
        StringArbitrary authorName = Arbitraries.strings();
        BigDecimalArbitrary authorWealth = Arbitraries.bigDecimals();
        IntegerArbitrary authorAge = Arbitraries.integers().between(0, 100);

        return Combinators.combine(authorName, authorAge, authorWealth).as(Author::new);
    }

    @Provide
    Arbitrary<Book> generateBooks() {
        StringArbitrary bookName = Arbitraries.strings();
        Arbitrary<Author> authorArbitrary = generateAuthors();
        IntegerArbitrary bookPages = Arbitraries.integers().greaterOrEqual(1);

        return Combinators.combine(bookName, authorArbitrary, bookPages).as(Book::new);
    }
}
