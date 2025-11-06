package com.library.management.service;


import com.library.management.dto.BorrowerDTO;
import com.library.management.entity.Borrower;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class BorrowerService {


    private final BorrowerRepository borrowerRepo;


    public BorrowerService(BorrowerRepository borrowerRepo) { this.borrowerRepo = borrowerRepo; }


    @Transactional
    public BorrowerDTO create(BorrowerDTO dto) {
        Borrower b = Borrower.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .membershipType(dto.getMembershipType() == null ? com.library.management.entity.MembershipType.BASIC : com.library.management.entity.MembershipType.valueOf(dto.getMembershipType()))
                .build();
        Borrower saved = borrowerRepo.save(b);
        dto.setId(saved.getId());
        return dto;
    }


    public BorrowerDTO getById(UUID id) {
        Borrower b = borrowerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));
        BorrowerDTO d = new BorrowerDTO();
        d.setId(b.getId()); d.setName(b.getName()); d.setEmail(b.getEmail()); d.setMembershipType(b.getMembershipType().name());
        return d;
    }
}