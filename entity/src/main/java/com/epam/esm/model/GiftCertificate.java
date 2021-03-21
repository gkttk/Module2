package com.epam.esm.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

public class GiftCertificate {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final int duration;
    private final Date createDate;
    private final Date lastUpdateDate;


    public GiftCertificate(String name, String description, BigDecimal price, int duration, Date createDate, Date lastUpdateDate) {
        this.id = null;//todo mb not final fields?
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate(Long id, String name, String description, BigDecimal price, int duration, Date createDate, Date lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        GiftCertificate that = (GiftCertificate) o;
        return duration == that.duration &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", GiftCertificate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .toString();
    }
}
