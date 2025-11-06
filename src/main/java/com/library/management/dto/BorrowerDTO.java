package com.library.management.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Data
@Getter
@Setter
public class BorrowerDTO {
    private UUID id;
    private String name;
    private String email;
    private String membershipType;
    private int maxBorrowLimit;
}