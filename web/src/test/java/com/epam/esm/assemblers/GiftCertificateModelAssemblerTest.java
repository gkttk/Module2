package com.epam.esm.assemblers;


import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


public class GiftCertificateModelAssemblerTest extends AbstractModelAssemblerTest<GiftCertificateDto> {

    @Autowired
    private GiftCertificateModelAssembler modelAssembler;


    @Override
    protected GiftCertificateModelAssembler getModelAssembler() {
        return modelAssembler;
    }

    @Override
    protected GiftCertificateDto getDto() {
        return new GiftCertificateDto().toBuilder()
                .id(100L)
                .name("testCertificate")
                .description("description")
                .price(BigDecimal.TEN)
                .duration(10)
                .build();
    }


}
