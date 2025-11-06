package com.library.management.repository;


import com.library.management.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;


public interface BorrowerRepository extends JpaRepository<Borrower, UUID> {
}