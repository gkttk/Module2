package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/certificates", produces = "application/json")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAll() {
        List<GiftCertificateDto> certificates = giftCertificateService.findAll();
        if (certificates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.getById(id);
        if (certificate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(certificate);

    }

    @DeleteMapping("/{id}")  //todo check isDelete
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Void> createCertificate(@RequestBody @Valid GiftCertificateDto certificateDto,
                                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        giftCertificateService.save(certificateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCertificate(@RequestBody @Valid GiftCertificateDto giftCertificateDto,
                                                  @PathVariable long id) {
        giftCertificateService.update(giftCertificateDto, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchCertificate(@RequestBody @Valid GiftCertificatePatchDto giftCertificatePatchDto,
                                                 @PathVariable long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        giftCertificateService.patch(giftCertificatePatchDto, id);
        return ResponseEntity.noContent().build();
    }







   /* @PostMapping(params = "newTags") //add to dto taglist
    @ResponseStatus(HttpStatus.CREATED)
    public void createCertificateAndNewTags(@RequestBody GiftCertificateDto certificateDto,
                                            @RequestParam(name = "newTags") String[] tagNames){
        Arrays.stream(tagNames).
                forEach(name-> restTemplate.postForObject("http://localhost:8080/tags",name, TagDto.class));
        createCertificate(certificateDto);
    }*/


}
