package iob.converters;

import java.util.Map;
import javax.persistence.AttributeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapToStringConverter implements AttributeConverter<Map<String, Object>, String>{
	private ObjectMapper jackson;
	
	public MapToStringConverter() {
		this.jackson = new ObjectMapper();
	}
	
	@Override
	public String convertToDatabaseColumn(Map<String, Object> jsonFromEntity) {
		// marshalling
		try {
			return this.jackson.writeValueAsString(jsonFromEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String stringFromDb) {
		// unmarshalling
		try {
			return this.jackson.readValue(stringFromDb, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
