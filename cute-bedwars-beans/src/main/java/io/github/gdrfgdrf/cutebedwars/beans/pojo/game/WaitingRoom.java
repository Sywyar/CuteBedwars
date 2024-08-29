package io.github.gdrfgdrf.cutebedwars.beans.pojo.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.gdrfgdrf.cutebedwars.beans.annotation.Undefinable;
import io.github.gdrfgdrf.cutebedwars.beans.base.PropertyConvertible;
import io.github.gdrfgdrf.cutebedwars.beans.pojo.common.Coordinate;
import io.github.gdrfgdrf.cutebedwars.beans.pojo.common.Region;
import lombok.Data;

/**
 * @author gdrfgdrf
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaitingRoom implements PropertyConvertible {
    private Region region;
    @JsonProperty(value = "spawnpoint-coordinate")
    private Coordinate spawnpointCoordinate;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(Class<?> targetType, Object obj) {
        if (targetType == Region.class) {
            Region region = new Region();
            return (T) region.convert(targetType, obj);
        }
        if (targetType == Coordinate.class) {
            Coordinate coordinate = new Coordinate();
            return (T) coordinate.convert(targetType, obj);
        }
        return null;
    }
}
