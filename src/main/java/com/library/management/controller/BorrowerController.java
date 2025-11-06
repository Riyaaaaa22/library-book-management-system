package com.library.management.controller;


import com.library.management.dto.BorrowerDTO;
import com.library.management.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/api/library/borrowers")
public class BorrowerController {


    private final BorrowerService borrowerService;


    public BorrowerController(BorrowerService borrowerService) { this.borrowerService = borrowerService; }


    @PostMapping
    public ResponseEntity<BorrowerDTO> create(@RequestBody BorrowerDTO dto) {
        BorrowerDTO res = borrowerService.create(dto);
        return ResponseEntity.status(201).body(res);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BorrowerDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(borrowerService.getById(id));
    }
}