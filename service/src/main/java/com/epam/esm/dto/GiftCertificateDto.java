package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.SaveGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * This DTO is validated to fully update a GiftCertificate entity.
 *
 * @since 1.0
 */

@Relation(itemRelation = "gift_certificate", collectionRelation = "gift_certificates")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto>  {

    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "GiftCertificate's id value must be null")
    @NotNull(groups = {SaveGroup.class}, message = "GiftCertificate's id value must not be null")
    private Long id;
    @NotBlank(groups = UpdateGroup.class, message = "GiftCertificate's name value must contain 2-35 characters and not be null")
    @Pattern(regexp="^[0-9A-Za-z\\s]*$", message = "GiftCertificate's name value must contain 2-35 characters and not be null", groups = {UpdateGroup.class, PatchGroup.class})
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

}
