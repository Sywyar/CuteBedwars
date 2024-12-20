package io.github.gdrfgdrf.cutebedwars.beans.pojo.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.gdrfgdrf.cutebedwars.beans.annotation.ConvertPropertyFunction;
import lombok.Data;

/**
 * @author gdrfgdrf
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinate {
    private double x;
    private double y;
    private double z;

    public Coordinate blockCoordinate() {
        Coordinate coordinate = new Coordinate();
        coordinate.x = (int) x;
        coordinate.y = (int) y;
        coordinate.z = (int) z;
        return coordinate;
    }

    @Override
    public String toString() {
        return "(%s, %s, %s)".formatted(x, y, z);
    }

    public static Coordinate parse(String string) {
        String[] split = string.split(" ");
        if (split.length != 3) {
            throw new IllegalArgumentException("the length of a string separated by a space must be 3");
        }
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);

        Coordinate result = new Coordinate();
        result.x = x;
        result.y = y;
        result.z = z;
        return result;
    }

    @SuppressWarnings("unchecked")
    @ConvertPropertyFunction
    public static <T> T convert(Class<?> targetType, Object obj) {
        return (T) Double.valueOf(Double.parseDouble(obj.toString()));
    }
}
