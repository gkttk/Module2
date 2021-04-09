package com.epam.esm.advice.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonIgnoreProperties(value = {"status"})
public enum ResponseError {
    GIFT_CERTIFICATE_NOT_FOUND("Gift certificate is not found", HttpStatus.NOT_FOUND, 40401),
    GIFT_CERTIFICATE_WITH_SUCH_NAME_EXISTS("Gift certificate already exists", HttpStatus.BAD_REQUEST, 42010),

    TAG_NOT_FOUND("Tag is not found", HttpStatus.NOT_FOUND, 40402),
    TAG_WITH_SUCH_NAME_EXISTS("Tag already exists", HttpStatus.BAD_REQUEST, 42000),

    INCORRECT_GIFT_CERTIFICATE_SORTING_FIELD("Incorrect field for sorting GiftCertificate was passed", HttpStatus.BAD_REQUEST, 44601),
    INCORRECT_TAG_SORTING_FIELD("Incorrect field for sorting Tag was passed", HttpStatus.BAD_REQUEST, 44602);

    private final String message;
    private final int code;
    private final HttpStatus status;

    ResponseError(String message, HttpStatus status, int code) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
