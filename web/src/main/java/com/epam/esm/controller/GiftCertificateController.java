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

    @GetMapping(params = "partOfDescription")
    public ResponseEntity<List<GiftCertificateDto>> getAllByPartOfDescription(@RequestParam String partOfDescription) {
        List<GiftCertificateDto> certificates = giftCertificateService.getAllByPartOfDescription(partOfDescription);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping(params = "partOfName")
    public ResponseEntity<List<GiftCertificateDto>> getAllByPartOfName(@RequestParam String partOfName) {
        List<GiftCertificateDto> certificates = giftCertificateService.getAllByPartOfName(partOfName);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping(params = {"sortFields", "sortOrder"})
    public ResponseEntity<List<GiftCertificateDto>> getAllSorted(@RequestParam List<String> sortFields,
                                                                 @RequestParam String sortOrder) {
        List<GiftCertificateDto> sortedCertificates = giftCertificateService.getAllSorted(sortFields, sortOrder);
        return ResponseEntity.ok(sortedCertificates);
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAll() {
        List<GiftCertificateDto> certificates = giftCertificateService.getAll();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.getById(id);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping(params = "tagName")
    public ResponseEntity<List<GiftCertificateDto>> getAllByTagName(@RequestParam String tagName) {
        List<GiftCertificateDto> certificates = giftCertificateService.getAllByTagName(tagName);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")  //todo check isDelete
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@RequestBody @Valid GiftCertificateDto certificateDto,
                                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        GiftCertificateDto savedCertificate = giftCertificateService.save(certificateDto);
        return new ResponseEntity<>(savedCertificate, HttpStatus.CREATED);
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

}
