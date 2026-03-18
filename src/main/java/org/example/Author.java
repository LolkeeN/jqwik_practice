package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private String name;
    private int age;
    private BigDecimal wealth;

    public boolean isOld() {
        return age >= 60;
    }
}
