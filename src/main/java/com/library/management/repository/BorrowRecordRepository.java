package com.library.management.repository;


import com.library.management.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, UUID> {
    List<BorrowRecord> findByBorrowerIdAndReturnDateIsNull(UUID borrowerId);
    List<BorrowRecord> findByReturnDateIsNull();
}