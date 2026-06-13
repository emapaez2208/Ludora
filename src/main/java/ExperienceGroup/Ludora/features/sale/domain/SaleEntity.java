package ExperienceGroup.Ludora.features.sale.domain;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "sales")
public class SaleEntity{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ESaleStatus status;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column (name = "earned_points", nullable = false)
    private Integer earnedPoints = 0;

    @Column (name = "has_discount", nullable = false)
    private Boolean hasDiscount = false;

    @ManyToOne
    @JoinColumn (name = "client_id")
    private ClientEntity client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItemEntity> items;

    @PrePersist
    void OnCreate(){
        if (date == null)
            date = LocalDateTime.now();

        if(externalId == null)
            externalId = UUID.randomUUID();

    }

}
