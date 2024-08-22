package io.github.gdrfgdrf.cutebedwars.beans.pojo.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.gdrfgdrf.cutebedwars.beans.annotation.Undefinable;
import io.github.gdrfgdrf.cutebedwars.beans.base.PropertyConvertible;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gdrfgdrf
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Area implements PropertyConvertible {
    @Undefinable
    private Long id;
    @Undefinable
    @JsonProperty(value = "default-template-id")
    private Long defaultTemplateId;

    private String name;
    @JsonProperty(value = "world-name")
    private String worldName;
    @Undefinable
    private List<Game> games = new ArrayList<>();


    @Override
    public Object convert(Class<?> targetType, Object obj) {
        if (targetType == String.class) {
            return obj.toString();
        }
        return null;
    }
}
