package com.epam.esm.dto;

import com.epam.esm.dto.groups.SaveOrderGroup;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;

@Relation(itemRelation = "order", collectionRelation = "orders")
public class OrderDto extends RepresentationModel<OrderDto> {

    @Null(message = "Order's id value must be null", groups = {SaveOrderGroup.class})
    private Long id;
    @Null(groups = SaveOrderGroup.class, message = "Order's cost must be null")
    private BigDecimal cost;
    @Null(groups = {SaveOrderGroup.class}, message = "Order's creation date value must be null")
    private String creationDate;
    @NotNull(groups = SaveOrderGroup.class, message = "Order's gift certificates value must be not null")
    @Valid
    private List<GiftCertificateDto> giftCertificates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<GiftCertificateDto> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificateDto> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

}
