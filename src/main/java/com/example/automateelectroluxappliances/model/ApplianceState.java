package com.example.automateelectroluxappliances.model;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ApplianceState {
    private String applianceId;
    private String connectionState;
    private String status;
    private Properties properties;
    public static class Properties {
        private Reported reported;

        public Reported getReported() {
            return reported;
        }

        public void setReported(Reported reported) {
            this.reported = reported;
        }
    }

    @JsonDeserialize(using = DynamicPropertiesDeserializer.class)
    public static class Reported {
        private Map<String, Object> properties = new HashMap<>();

        @JsonAnySetter
        public void setProperties(String key, Object value) {
            properties.put(key, value);
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    public String getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(String applianceId) {
        this.applianceId = applianceId;
    }

    public String getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(String connectionState) {
        this.connectionState = connectionState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}