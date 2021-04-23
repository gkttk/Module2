package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * This DTO is validated to fully update a Tag entity.
 *
 * @since 1.0
 */
@Relation(itemRelation = "tag", collectionRelation = "tags")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {

    @Null(message = "Tag's id value must be null", groups = {UpdateGroup.class, PatchGroup.class})
    private Long id;

    @NotBlank(message = "Tag's name value must contain 2-15 characters and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    @Size(min = 2, max = 15, message = "Tag's name value must contain 2-15 characters and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    private String name;
}
