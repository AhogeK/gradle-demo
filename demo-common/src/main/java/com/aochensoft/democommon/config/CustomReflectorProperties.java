package com.aochensoft.democommon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-10 17:04:38
 */
@Data
@ConfigurationProperties(prefix = "reflector")
public class CustomReflectorProperties {

    private boolean trace;
}
