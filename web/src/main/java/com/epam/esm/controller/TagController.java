package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/tags", produces = "application/json")
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
    public ResponseEntity<List<TagDto>> getAll() {
        List<TagDto> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable long id) {
        TagDto tag = tagService.getById(id);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TagDto> createTag(@RequestBody @Valid TagDto tagDto) {
        TagDto savedTag = tagService.save(tagDto);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);

    }


}
