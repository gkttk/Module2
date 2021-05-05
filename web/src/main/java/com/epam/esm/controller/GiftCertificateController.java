package com.epam.esm.controller;

import com.epam.esm.assemblers.GiftCertificateModelAssembler;
import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/certificates", produces = "application/hal+json")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final ModelAssembler<GiftCertificateDto> assembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateModelAssembler assembler) {
        this.giftCertificateService = giftCertificateService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<GiftCertificateDto>> getAllForQuery(WebRequest webRequest,
                                                                              @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_LIMIT + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                              @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_OFFSET + "") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        List<GiftCertificateDto> certificates = giftCertificateService.findAllForQuery(parameterMap, limit, offset);
        return ResponseEntity.ok(assembler.toCollectionModel(certificates, offset));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.findById(id);
        return ResponseEntity.ok(assembler.toModel(certificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto certificateDto) {
        GiftCertificateDto certificate = giftCertificateService.save(certificateDto);
        return ResponseEntity.ok(assembler.toModel(certificate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                                @PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.update(giftCertificateDto, id);
        return ResponseEntity.ok(assembler.toModel(certificate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> patchCertificate(@RequestBody @Validated(PatchGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                               @PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.patch(giftCertificateDto, id);
        return ResponseEntity.ok(assembler.toModel(certificate));
    }


}
