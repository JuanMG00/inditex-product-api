package com.inditex.domain;

import com.inditex.domain.enums.Zone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class PriceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    @NotNull
    private BigDecimal rate;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Zone zone;
}
