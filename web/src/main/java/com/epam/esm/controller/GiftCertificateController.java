package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public List<GiftCertificateDto> getAll() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificateDto getById(@PathVariable long id) {
        return giftCertificateService.getById(id);
    }

    @DeleteMapping("/certificates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
    }


    @PostMapping("/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCertificate(@RequestBody GiftCertificateDto certificateDto) {
        giftCertificateService.save(certificateDto);
    }

    @PutMapping("/certificates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCertificate(@RequestBody GiftCertificateDto giftCertificateDto, @PathVariable long id) {
        giftCertificateService.update(giftCertificateDto, id);
    }


}
