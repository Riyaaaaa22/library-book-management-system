package com.library.management.service;


import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.exception.BadRequestException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {


    private final BookRepository bookRepo;


    public BookService(BookRepository bookRepo) { this.bookRepo = bookRepo; }


    @Transactional
    public BookDTO createOrIncrease(BookDTO dto) {
        if (dto.getTitle() == null) throw new BadRequestException("Title required");
        if (bookRepo.existsByTitleAndDeletedFalse(dto.getTitle())) {
            Book existing = bookRepo.findByTitleAndDeletedFalse(dto.getTitle());
            existing.setTotalCopies(existing.getTotalCopies() + dto.getTotalCopies());
            existing.setAvailableCopies(existing.getAvailableCopies() + dto.getAvailableCopies());
            bookRepo.save(existing);
            return mapToDto(existing);
        } else {
            Book b = Book.builder()
                    .title(dto.getTitle())
                    .author(dto.getAuthor())
                    .category(dto.getCategory())
                    .totalCopies(dto.getTotalCopies())
                    .availableCopies(dto.getAvailableCopies())
                    .deleted(false)
                    .build();
            Book saved = bookRepo.save(b);
            return mapToDto(saved);
        }
    }

    public Page<BookDTO> list(String category, Boolean available, int page, int size, String sortBy, String dir) {
        Sort sort = Sort.by(dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> p = bookRepo.findByFilter(category, available, pageable);
        return p.map(this::mapToDto);
    }


    @Transactional
    public BookDTO update(UUID id, BookDTO dto) {
        Book b = bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (dto.getTitle() != null) b.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) b.setAuthor(dto.getAuthor());
        if (dto.getCategory() != null) b.setCategory(dto.getCategory());
        if (dto.getTotalCopies() > 0) b.setTotalCopies(dto.getTotalCopies());
        b.setAvailableCopies(dto.getAvailableCopies());
        Book saved = bookRepo.save(b);
        return mapToDto(saved);
    }


    @Transactional
    public void softDelete(UUID id) {
        Book b = bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        boolean hasActive = b.getBorrowRecords().stream().anyMatch(r -> r.getReturnDate() == null);
        if (hasActive) throw new BadRequestException("Cannot delete book with active borrow records");
        b.setDeleted(true);
        bookRepo.save(b);
    }


    private BookDTO mapToDto(Book b) {
        BookDTO d = new BookDTO();
        d.setId(b.getId());
        d.setTitle(b.getTitle());
        d.setAuthor(b.getAuthor());
        d.setCategory(b.getCategory());
        d.setTotalCopies(b.getTotalCopies());
        d.setAvailableCopies(b.getAvailableCopies());
        return d;
    }
}