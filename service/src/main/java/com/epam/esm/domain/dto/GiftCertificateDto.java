package com.epam.esm.domain.dto;

import com.epam.esm.domain.dto.groups.PatchGroup;
import com.epam.esm.domain.dto.groups.SaveGroup;
import com.epam.esm.domain.dto.groups.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

/**
 * GiftCertificate DTO.
 *
 * @since 1.0
 */

@Relation(itemRelation = "gift_certificate", collectionRelation = "gift_certificates")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "{gift_certificate_id_violation_message}")
    @NotNull(groups = {SaveGroup.class}, message = "{gift_certificate_id_violation_message}")
    private Long id;
    @NotBlank(groups = UpdateGroup.class, message = "{gift_certificate_name_violation_message}")
    @Pattern(regexp = "^[0-9A-Za-z\\s]*$", message = "{gift_certificate_name_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    @Size(min = 2, max = 35, message = "{gift_certificate_name_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    private String name;
    @NotBlank(groups = UpdateGroup.class, message = "{gift_certificate_description_violation_message}")
    @Size(min = 20, max = 150, message = "{gift_certificate_description_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    private String description;
    @NotNull(groups = UpdateGroup.class, message = "{gift_certificate_price_violation_message}")
    @DecimalMin(value = "0.1", message = "{gift_certificate_price_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    @DecimalMax(value = "1000", message = "{gift_certificate_price_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    private BigDecimal price;
    @NotNull(groups = UpdateGroup.class, message = "{gift_certificate_duration_violation_message}")
    @Min(value = 1, groups = {UpdateGroup.class, PatchGroup.class}, message = "{gift_certificate_duration_violation_message}")
    @Max(value = 100, groups = {UpdateGroup.class, PatchGroup.class}, message = "{gift_certificate_duration_violation_message}")
    private Integer duration;
    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "{gift_certificate_create_date_violation_message}")
    private String createDate;
    @Null(groups = {UpdateGroup.class, PatchGroup.class}, message = "{gift_certificate_last_update_date_violation_message}")
    private String lastUpdateDate;

    @Valid
    private List<TagDto> tags;

}
