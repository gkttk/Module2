package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * This DTO is validated to fully update a GiftCertificate entity.
 *
 * @since 1.0
 */
public class GiftCertificateDto {

    @Null(groups = {UpdateGroup.class, PatchGroup.class})
    private Long id;
    @NotBlank(groups = UpdateGroup.class)
    @Size(min = 2, max = 35, message = "name should contain 2-35 characters", groups = {UpdateGroup.class, PatchGroup.class})
    private String name;
    @NotBlank(groups = UpdateGroup.class)
    @Size(max = 150, message = "description should contain 0-150 characters", groups = {UpdateGroup.class, PatchGroup.class})
    private String description;
    @NotNull(groups = UpdateGroup.class)
    @DecimalMin(value = "0.0", message = "min price = 0", groups = {UpdateGroup.class, PatchGroup.class})
    @DecimalMax(value = "1000", message = "max price = 1000", groups = {UpdateGroup.class, PatchGroup.class})
    private BigDecimal price;
    @NotNull(groups = UpdateGroup.class)
    @Min(value = 1, groups = {UpdateGroup.class, PatchGroup.class})
    @Max(value = 100, groups = {UpdateGroup.class, PatchGroup.class})
    private Integer duration;
    @Null(groups = {UpdateGroup.class, PatchGroup.class})
    private LocalDateTime createDate;
    @Null(groups = {UpdateGroup.class, PatchGroup.class})
    private LocalDateTime lastUpdateDate;

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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
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
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("tags=" + tags)
                .toString();
    }
}
