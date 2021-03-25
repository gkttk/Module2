package com.epam.esm.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class GiftCertificatePatchDto {

    @Size(min = 2, max = 35, message = "name should contain 2-35 characters")
    private String name;
    @Size(max = 150, message = "description should contain 0-150 characters")
    private String description;
    @DecimalMin(value = "0.0", message = "min price = 0")
    @DecimalMax(value = "1000", message = "max price = 1000")
    private BigDecimal price;
    @Min(1)
    @Max(100)
    private Integer duration;

    public GiftCertificatePatchDto() {
    }

    public GiftCertificatePatchDto(String name, String description, BigDecimal price, Integer duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
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

    public Integer getDuration() {
        return duration;
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

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificatePatchDto that = (GiftCertificatePatchDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GiftCertificatePatchDto.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .toString();
    }
}
