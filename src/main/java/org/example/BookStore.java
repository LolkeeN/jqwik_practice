package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStore {
    private Map<Author, Set<Book>> collection = new HashMap<>();

    public void addBook(Author author, Book book) {
        Set<Book> authorCollection = collection.getOrDefault(author, new HashSet<>());
        authorCollection.add(book);

        collection.put(author, authorCollection);
    }

    public boolean hasAuthor(Author author) {
        return collection.containsKey(author);
    }

    public boolean hasAuthor(String authorName) {
        return collection.keySet().stream()
                .anyMatch(author -> Objects.equals(authorName, author.getName()));
    }

    public void removeBook(String authorName, Book book) throws RuntimeException {
        Map.Entry<Author, Set<Book>> authorCollection = collection.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getKey().getName(), authorName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Author is not present"));

        authorCollection.getValue().remove(book);
        collection.replace(authorCollection.getKey(), authorCollection.getValue());
    }

    public boolean containsBookByAuthor(Book book, Author author) {
        return collection.getOrDefault(author, new HashSet<>()).contains(book);
    }

    public void removeAuthor(Author author) {
        collection.remove(author);
    }
}
