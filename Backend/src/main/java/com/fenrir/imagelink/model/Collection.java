package com.fenrir.imagelink.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String url;

    @Column(nullable = false, length = 32)
    private String secretKey;

    @Column(nullable = false)
    private Boolean hidden;

    @Column(nullable = false)
    private Long lifePeriod;

    @Column(nullable = false, length = 254)
    private String title;

    @Column(length = 2048)
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Collection(
            String url,
            String secretKey,
            Boolean hidden,
            Long lifePeriod,
            String title,
            String description) {

        this.url = url;
        this.secretKey = secretKey;
        this.hidden = hidden;
        this.lifePeriod = lifePeriod;
        this.title = title;
        this.description = description;
    }
}
