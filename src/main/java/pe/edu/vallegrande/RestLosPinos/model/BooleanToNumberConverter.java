package pe.edu.vallegrande.RestLosPinos.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToNumberConverter implements AttributeConverter<Boolean, Integer> {

    public BooleanToNumberConverter() {
        // Constructor público sin argumentos obligatorio
    }

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.equals(1);
    }
}
