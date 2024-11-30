package io.github.gdrfgdrf.cutebedwars.database.impl.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.gdrfgdrf.cutebedwars.abstracts.commons.IConfig;
import lombok.Data;

/**
 * @author gdrfgdrf
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config implements IConfig {
    public static Config instance = null;

    @JsonProperty(value = "enable-database-logging")
    private Boolean enableDatabaseLogging;

    @JsonProperty(value = "database-username")
    private String databaseUsername;

    @JsonProperty(value = "database-password")
    private String databasePassword;

    public static void reset(Config config) {
        config.databaseUsername = null;
        config.databasePassword = null;
        config.enableDatabaseLogging = null;
    }

    @Override
    public void fulfill() {
        enableDatabaseLogging = false;
        databaseUsername = "";
        databasePassword = "";
    }

    public static <T> T value(String key) {
        return instance.get(key);
    }
}