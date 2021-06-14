package com.epam.esm.domain.dto.bundles;

import com.epam.esm.domain.dto.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class TagDtoBundle {
    private final List<TagDto> tags;
    private final long count;
}
