package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(params = "tagName")
    public ResponseEntity<TagDto> getByName(@RequestParam String tagName){
        Optional<TagDto> tagByNameOpt = tagService.findByName(tagName);
        if (tagByNameOpt.isPresent()){
            TagDto tagDto = tagByNameOpt.get();
            return new ResponseEntity<>(tagDto, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @GetMapping
    public List<TagDto> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public TagDto getById(@PathVariable long id) {
        return tagService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        tagService.delete(id);
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCertificate(@RequestBody TagDto tagDto) {
        tagService.save(tagDto);
    }


}
