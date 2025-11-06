package com.library.management.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Data
@Getter
@Setter
public class BorrowRecordDTO {
    private UUID id;
    private UUID bookId;
    private UUID borrowerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Double fineAmount;
}