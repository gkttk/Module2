package com.epam.esm.dto.bundles;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class TagDtoBundle {
    private final List<TagDto> tags;
    private final long count;
}
