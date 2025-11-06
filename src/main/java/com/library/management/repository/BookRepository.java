package com.library.management.repository;


import com.library.management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.UUID;


public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByCategory(String category);


    @Query("select b from Book b where (:category is null or b.category = :category) and (:available is null or b.availableCopies > 0) and b.deleted = false")
    Page<Book> findByFilter(@Param("category") String category, @Param("available") Boolean available, Pageable pageable);


    boolean existsByTitleAndDeletedFalse(String title);


    Book findByTitleAndDeletedFalse(String title);
}