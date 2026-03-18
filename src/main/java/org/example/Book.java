package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String title;
    private Author author;
    private int pages;

    public boolean isBig() {
        return pages >= 600;
    }
}
