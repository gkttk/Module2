package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
/**
 * This DTO is for making an order.
 *
 * @since 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrderDto{

    @NotNull(message = "CertificateId value must be not null")
    private Long certificateId;

    @NotNull(message = "Count value must be greater than 0 and not null")
    @Min(value = 1, message = "Count value must be greater than 0 and not null")
    private Integer count;
}
