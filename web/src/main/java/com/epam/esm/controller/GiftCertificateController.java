package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
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
        List<GiftCertificateDto> certificates = giftCertificateService.findAllForQuery(parameterMap);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.findById(id);
        return ResponseEntity.ok(certificate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto certificateDto) {
        GiftCertificateDto savedCertificate = giftCertificateService.save(certificateDto);
        return ResponseEntity.ok(savedCertificate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                                @PathVariable long id) {
        GiftCertificateDto updatedDto = giftCertificateService.update(giftCertificateDto, id);
        return ResponseEntity.ok(updatedDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> patchCertificate(@RequestBody @Validated(PatchGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                               @PathVariable long id) {
        GiftCertificateDto patchedDto = giftCertificateService.patch(giftCertificateDto, id);
        return ResponseEntity.ok(patchedDto);
    }





}
