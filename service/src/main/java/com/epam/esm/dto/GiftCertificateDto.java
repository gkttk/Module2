package com.epam.esm.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class GiftCertificateDto {

    @Null
    private Long id;
    @NotBlank
    @Size(min = 2, max = 35, message = "name should contain 2-35 characters")
    private String name;
    @NotBlank
    @Size(max = 150, message = "description should contain 0-150 characters")
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", message = "min price = 0")
    @DecimalMax(value = "1000", message = "max price = 1000")
    private BigDecimal price;
    @NotNull
    @Min(1)
    @Max(100)
    private int duration;
    @Null
    private Date createDate;
    @Null
    private Date lastUpdateDate;

    @Valid
    private List<TagDto> tags;


    public GiftCertificateDto() {
    }


    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        GiftCertificateDto that = (GiftCertificateDto) o;
        return duration == that.duration &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(tags, that.tags);
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
