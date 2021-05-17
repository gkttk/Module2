package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Tag DTO.
 *
 * @since 1.0
 */
@Relation(itemRelation = "tag", collectionRelation = "tags")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {

    @Null(message = "{tag_dto_id_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    private Long id;

    @NotBlank(message = "{tag_dto_name_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9\\s]*$", message = "{tag_dto_name_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    @Size(min = 2, max = 15, message = "{tag_dto_name_violation_message}", groups = {UpdateGroup.class, PatchGroup.class})
    private String name;
}
