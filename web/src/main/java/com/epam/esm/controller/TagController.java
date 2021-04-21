package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/tags", produces = "application/hal+json")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(params = "tagName")
    public ResponseEntity<TagDto> getByName(@RequestParam String tagName) {
        TagDto tag = tagService.findByName(tagName);
        return ResponseEntity.ok(tag);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<TagDto>>> getAll(WebRequest request,
                                                            @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                            @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParams = request.getParameterMap();
        List<TagDto> tags = tagService.findAllForQuery(reqParams, limit, offset);

        List<EntityModel<TagDto>> result = tags.stream()
                .map(this::getEntityModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TagDto>> getById(@PathVariable long id) {
        TagDto tag = tagService.findById(id);
        return ResponseEntity.ok(getEntityModel(tag));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<TagDto>> createTag(@RequestBody @Validated({PatchGroup.class, UpdateGroup.class}) @Valid TagDto tagDto) {
        TagDto tag = tagService.save(tagDto);
        return ResponseEntity.ok(getEntityModel(tag));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<TagDto> getEntityModel(TagDto tag) {
        Long id = tag.getId();
        return EntityModel.of(tag,
                linkTo(methodOn(TagController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(TagController.class).deleteById(id)).withRel("delete"));
    }


}
