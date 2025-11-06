package com.library.management.controller;


import com.library.management.dto.BorrowRecordDTO;
import com.library.management.service.BorrowRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/library/records")
public class BorrowRecordController {


    private final BorrowRecordService recordService;


    public BorrowRecordController(BorrowRecordService recordService) { this.recordService = recordService; }


    @PostMapping("/borrow/{bookId}/{borrowerId}")
    public ResponseEntity<BorrowRecordDTO> borrow(@PathVariable UUID bookId, @PathVariable UUID borrowerId) {
        return ResponseEntity.status(201).body(recordService.borrow(bookId, borrowerId));
    }


    @PostMapping("/return/{recordId}")
    public ResponseEntity<BorrowRecordDTO> ret(@PathVariable UUID recordId) {
        return ResponseEntity.ok(recordService.returnBook(recordId));
    }


    @GetMapping("/active")
    public ResponseEntity<List<BorrowRecordDTO>> active() {
        return ResponseEntity.ok(recordService.getActiveRecords());
    }
}