package com.p11.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "p11.masking")
public class MaskingProperties {

    /**
     * Initial properties of our mask
     */

    private boolean enabled = true;
    private List<String> fields = new ArrayList<>();
    private MaskOptions maskStyle = MaskOptions.FULL;
    private String maskCharacter = "*";
}
