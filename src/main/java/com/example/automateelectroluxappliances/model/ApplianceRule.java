package com.example.automateelectroluxappliances.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

public class ApplianceRule {

    @Id
    private String id;
    private String applianceId;
    private List<Action> action;
    private List<Condition> condition;

    public static class Action {
        private Map<String, Object> executeCommand;

        public Map<String, Object> getExecuteCommand() {
            return executeCommand;
        }

        public void setExecuteCommand(Map<String, Object> executeCommand) {
            this.executeCommand = executeCommand;
        }
    }

    public static class Condition {
        private String property;
        private Object value;
        private String operator;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Action> getAction() {
        return action;
    }

    public void setAction(List<Action> action) {
        this.action = action;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }

    public String getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(String applianceId) {
        this.applianceId = applianceId;
    }
}
