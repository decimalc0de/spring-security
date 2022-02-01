package com.decimalcode.qmed.api._repositories;

import com.decimalcode.qmed.api.counsel.service.CounselModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICounselRepository extends JpaRepository<CounselModel, Long> {
    Optional<CounselModel> findByName(String diagnosis);
}
