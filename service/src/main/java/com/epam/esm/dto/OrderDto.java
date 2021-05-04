package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order DTO.
 *
 * @since 2.0
 */
@Relation(itemRelation = "order", collectionRelation = "orders")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;
    private BigDecimal cost;
    private String creationDate;
    private List<GiftCertificateDto> giftCertificates;
}
