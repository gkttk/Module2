package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

@RestController
@RequestMapping(path = "/tags", produces = "application/hal+json")
@Validated
public class TagController {

    private final TagService tagService;
    private final ModelAssembler<TagDto> assembler;

    @Autowired
    public TagController(TagService tagService, ModelAssembler<TagDto> assembler) {
        this.tagService = tagService;
        this.assembler = assembler;
    }

    @GetMapping(params = "tagName")
    public ResponseEntity<TagDto> getByName(@RequestParam String tagName) {
        TagDto tag = tagService.findByName(tagName);
        return ResponseEntity.ok(assembler.toModel(tag));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<TagDto>> getAllForQuery(WebRequest request,
                                                                  @RequestParam(required = false, defaultValue = ApplicationConstants.DEFAULT_LIMIT + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                  @RequestParam(required = false, defaultValue = ApplicationConstants.DEFAULT_OFFSET + "") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParams = request.getParameterMap();
        List<TagDto> tags = tagService.findAllForQuery(reqParams, limit, offset);

        return ResponseEntity.ok(assembler.toCollectionModel(tags, offset));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable long id) {
        TagDto tag = tagService.findById(id);
        return ResponseEntity.ok(assembler.toModel(tag));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TagDto> createTag(@RequestBody @Validated({PatchGroup.class, UpdateGroup.class}) @Valid TagDto tagDto) {
        TagDto tag = tagService.save(tagDto);
        return ResponseEntity.ok(assembler.toModel(tag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("{userId}/most_widely_used_tag")
    public ResponseEntity<CollectionModel<TagDto>> getMostWidelyUsedTagsOfUser(@PathVariable long userId) {
        List<TagDto> tags = tagService.findMostWidelyUsed(userId);
        return ResponseEntity.ok(assembler.toCollectionModel(tags, ApplicationConstants.DEFAULT_OFFSET));
    }

}
