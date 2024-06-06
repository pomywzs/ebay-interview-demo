package com.ebay.interview.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class FileResponseDTO {

    private Boolean success;
    private String message;

    @Tolerate
    public FileResponseDTO() {}
}
