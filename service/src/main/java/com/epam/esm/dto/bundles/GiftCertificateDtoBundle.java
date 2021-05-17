package com.epam.esm.dto.bundles;

import com.epam.esm.dto.GiftCertificateDto;
import lombok.Data;

import java.util.List;

@Data
public class GiftCertificateDtoBundle {
    private final List<GiftCertificateDto> giftCertificates;
    private final long count;
}
