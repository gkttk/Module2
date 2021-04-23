package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GiftCertificate entity.
 *
 * @since 1.0
 */
@Entity
@Table(name = "gift_certificate")
@Data
@NoArgsConstructor
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @CreationTimestamp
    @Column(name = "create_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP",
            nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "last_update_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
            nullable = false)
    private LocalDateTime lastUpdateDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "certificates_tags",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;
}
