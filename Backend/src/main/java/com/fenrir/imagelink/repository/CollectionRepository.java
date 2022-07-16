package com.fenrir.imagelink.repository;

import com.fenrir.imagelink.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findByCode(String code);
}
