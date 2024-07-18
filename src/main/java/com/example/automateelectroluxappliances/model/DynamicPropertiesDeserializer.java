package com.example.automateelectroluxappliances.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class DynamicPropertiesDeserializer extends JsonDeserializer<ApplianceState.Reported> {

    @Override
    public ApplianceState.Reported deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        ApplianceState.Reported reported = new ApplianceState.Reported();

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            reported.setProperties(field.getKey(), field.getValue().asText());
        }

        return reported;
    }

}
