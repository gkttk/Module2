package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.SaveOrderGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This DTO is validated to fully update a GiftCertificate entity.
 *
 * @since 1.0
 */
@Relation(itemRelation = "gift_certificate", collectionRelation = "gift_certificates")
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto>  {

    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's id value must be null")
    @NotNull(groups = {SaveOrderGroup.class}, message = "GiftCertificate's id value must not be null")
    private Long id;
    @NotBlank(groups = UpdateGroup.class, message = "GiftCertificate's name value must contain 2-35 characters and not be null")
    @Size(min = 2, max = 35, message = "GiftCertificate's name value must contain 2-35 characters and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    private String name;
    @NotBlank(groups = UpdateGroup.class, message = "GiftCertificate's description value must contain 20-150 characters and not be null")
    @Size(min = 20, max = 150, message = "GiftCertificate's description value must contain 20-150 characters and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    private String description;
    @NotNull(groups = UpdateGroup.class, message = "GiftCertificate's price value must be 0.1-1000 and not null")
    @DecimalMin(value = "0.1", message = "GiftCertificate's price value must be 0.1-1000 and not null", groups = {UpdateGroup.class, PatchGroup.class})
    @DecimalMax(value = "1000", message = "GiftCertificate's price value must be 0.1-1000 and not null", groups = {UpdateGroup.class, PatchGroup.class})
    private BigDecimal price;
    @NotNull(groups = UpdateGroup.class, message = "GiftCertificate's duration value must be 1-100 and not null")
    @Min(value = 1, groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's duration value must be 1-100 and not null")
    @Max(value = 100, groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's duration value must be 1-100 and not null")
    private Integer duration;
    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's create date value must be null")
    private String createDate;
    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's last update date value must be null")
    private String lastUpdateDate;

    @Valid
    private List<TagDto> tags;


    public GiftCertificateDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificateDto dto = (GiftCertificateDto) o;
        return Objects.equals(id, dto.id) &&
                Objects.equals(name, dto.name) &&
                Objects.equals(description, dto.description) &&
                Objects.equals(price, dto.price) &&
                Objects.equals(duration, dto.duration) &&
                Objects.equals(createDate, dto.createDate) &&
                Objects.equals(lastUpdateDate, dto.lastUpdateDate) &&
                Objects.equals(tags, dto.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GiftCertificateDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate='" + createDate + "'")
                .add("lastUpdateDate='" + lastUpdateDate + "'")
                .add("tags=" + tags)
                .toString();
    }
}
