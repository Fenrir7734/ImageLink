package com.fenrir.imagelink.repository;

import com.fenrir.imagelink.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
