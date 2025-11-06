package com.library.management.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Data
@Getter
@Setter
public class BookDTO {
    private UUID id;
    private String title;
    private String author;
    private String category;
    private Boolean isAvailable;
    private int totalCopies;
    private int availableCopies;
}