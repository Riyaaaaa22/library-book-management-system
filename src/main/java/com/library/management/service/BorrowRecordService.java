package com.library.management.service;


import com.library.management.dto.BorrowRecordDTO;
import com.library.management.entity.Book;
import com.library.management.entity.BorrowRecord;
import com.library.management.entity.Borrower;
import com.library.management.exception.BadRequestException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowRecordRepository;
import com.library.management.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BorrowRecordService {
    private final BorrowRecordRepository recordRepo;
    private final BookRepository bookRepo;
    private final BorrowerRepository borrowerRepo;
    private final double DEFAULT_FINE_PER_DAY = 10.0;


    public BorrowRecordService(BorrowRecordRepository recordRepo, BookRepository bookRepo, BorrowerRepository borrowerRepo) {
        this.recordRepo = recordRepo;
        this.bookRepo = bookRepo;
        this.borrowerRepo = borrowerRepo;
    }


    @Transactional
    public BorrowRecordDTO borrow(UUID bookId, UUID borrowerId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Borrower borrower = borrowerRepo.findById(borrowerId).orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));


        if (book.getAvailableCopies() <= 0) throw new BadRequestException("Book not available");


        List<BorrowRecord> active = recordRepo.findByBorrowerIdAndReturnDateIsNull(borrowerId);
        if (active.size() >= borrower.getMaxBorrowLimit()) throw new BadRequestException("Borrow limit reached");


        BorrowRecord record = BorrowRecord.builder()
                .book(book)
                .borrower(borrower)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .fineAmount(0.0)
                .build();


        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);
        BorrowRecord saved = recordRepo.save(record);
        return mapToDto(saved);
    }


    @Transactional
    public BorrowRecordDTO returnBook(UUID recordId) {
        BorrowRecord record = recordRepo.findById(recordId).orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));
        if (record.getReturnDate() != null) throw new BadRequestException("Book already returned");


        record.setReturnDate(LocalDate.now());
        long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), record.getReturnDate());
        if (daysLate > 0) record.setFineAmount(daysLate * DEFAULT_FINE_PER_DAY);
        else record.setFineAmount(0.0);


        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);
        BorrowRecord saved = recordRepo.save(record);
        return mapToDto(saved);
    }


    public List<BorrowRecordDTO> getActiveRecords() {
        return recordRepo.findByReturnDateIsNull().stream().map(this::mapToDto).collect(Collectors.toList());
    }


    private BorrowRecordDTO mapToDto(BorrowRecord r) {
        BorrowRecordDTO d = new BorrowRecordDTO();
        d.setId(r.getId());
        d.setBookId(r.getBook().getId());
        d.setBorrowerId(r.getBorrower().getId());
        d.setBorrowDate(r.getBorrowDate());
        d.setDueDate(r.getDueDate());
        d.setReturnDate(r.getReturnDate());
        d.setFineAmount(r.getFineAmount());
        return d;
    }
}
