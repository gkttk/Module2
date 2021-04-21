package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/certificates", produces = "application/hal+json")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<GiftCertificateDto>>> getAllForQuery(WebRequest webRequest,
                                                                                @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                                @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();

        List<GiftCertificateDto> certificates = giftCertificateService.findAllForQuery(parameterMap, limit, offset);

        List<EntityModel<GiftCertificateDto>> result = certificates.stream()
                .map(this::getEntityModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> getById(@PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.findById(id);
        return ResponseEntity.ok(getEntityModel(certificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificateDto>> createCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto certificateDto) {
        GiftCertificateDto certificate = giftCertificateService.save(certificateDto);
        return ResponseEntity.ok(getEntityModel(certificate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateCertificate(@RequestBody @Validated(UpdateGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                                             @PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.update(giftCertificateDto, id);
        return ResponseEntity.ok(getEntityModel(certificate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> patchCertificate(@RequestBody @Validated(PatchGroup.class) @Valid GiftCertificateDto giftCertificateDto,
                                                                            @PathVariable long id) {
        GiftCertificateDto certificate = giftCertificateService.patch(giftCertificateDto, id);
        return ResponseEntity.ok(getEntityModel(certificate));
    }

    private EntityModel<GiftCertificateDto> getEntityModel(GiftCertificateDto certificate) {
        Long id = certificate.getId();
        return EntityModel.of(certificate,
                linkTo(methodOn(GiftCertificateController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class).updateCertificate(null, id)).withRel("update"),
                linkTo(methodOn(GiftCertificateController.class).patchCertificate(null, id)).withRel("partial_update"),
                linkTo(methodOn(GiftCertificateController.class).deleteById(id)).withRel("delete"));
    }



}
