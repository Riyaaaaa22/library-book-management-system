package com.library.management.controller;


import com.library.management.dto.BookDTO;
import com.library.management.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/api/library/books")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService) { this.bookService = bookService; }


    @PostMapping
    public ResponseEntity<BookDTO> createOrIncrease(@RequestBody BookDTO dto) {
        BookDTO res = bookService.createOrIncrease(dto);
        return ResponseEntity.status(201).body(res);
    }


    @GetMapping
    public ResponseEntity<Page<BookDTO>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        return ResponseEntity.ok(bookService.list(category, available, page, size, sortBy, dir));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable UUID id, @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        bookService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}