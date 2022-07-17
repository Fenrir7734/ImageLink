package com.fenrir.imagelink.repository;

import com.fenrir.imagelink.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByCode(String code);
    List<Image> findAllByCollectionCode(String code);
    boolean existsByCode(String code);
}
