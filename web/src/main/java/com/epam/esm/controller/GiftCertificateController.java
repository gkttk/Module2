package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<GiftCertificateDto>> getAllForQuery(WebRequest webRequest) {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        List<GiftCertificateDto> certificates = giftCertificateService.getAllForQuery(parameterMap);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.getById(id);
        return ResponseEntity.ok(certificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@RequestBody @Valid GiftCertificateDto certificateDto) {

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
                                                 @PathVariable long id) {
        giftCertificateService.patch(giftCertificatePatchDto, id);
        return ResponseEntity.noContent().build();
    }

}
