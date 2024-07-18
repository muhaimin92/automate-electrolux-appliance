package com.example.automateelectroluxappliances.service;

import com.example.automateelectroluxappliances.model.ApplianceRule;
import com.example.automateelectroluxappliances.model.ApplianceState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplianceRuleEngine {

    private static final List<String> VALID_OPERATORS = List.of("eq", "neq", "gt", "lt", "gte", "lte");

    public void validateRule(ApplianceRule applianceRule) {
        if (applianceRule.getCondition() == null) {
            throw new IllegalArgumentException("Condition is required.");
        }

        List<ApplianceRule.Condition> conditions = applianceRule.getCondition();

        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("Condition is required.");
        }
        for (ApplianceRule.Condition condition : conditions) {
            if (condition.getProperty() == null || condition.getProperty().isEmpty()) {
                throw new IllegalArgumentException("Property is required.");
            }

            if (condition.getOperator() == null || condition.getOperator().isEmpty()) {
                throw new IllegalArgumentException("Operator is required.");
            }

            if (!VALID_OPERATORS.contains(condition.getOperator())) {
                throw new IllegalArgumentException("Invalid operator: " + condition.getOperator());
            }

            if (condition.getValue() == null) {
                throw new IllegalArgumentException("Value is required.");
            }
        }

        if (applianceRule.getAction() == null || applianceRule.getAction().isEmpty()) {
            throw new IllegalArgumentException("Action is required.");
        }

        for (ApplianceRule.Action action : applianceRule.getAction()) {
            if (action.getExecuteCommand() == null || action.getExecuteCommand().isEmpty()) {
                throw new IllegalArgumentException("ExecuteCommand is required.");
            }
        }
    }

    public boolean evaluateCondition(List<ApplianceRule.Condition> conditions, ApplianceState state) {
        for (ApplianceRule.Condition condition : conditions) {
            Object propertyValue = state.getProperties().getReported().getProperties().get(condition.getProperty());
            if (!evaluateSingleCondition(condition, propertyValue)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateSingleCondition(ApplianceRule.Condition condition, Object propertyValue) {
        switch (condition.getOperator()) {
            case "eq":
                return propertyValue.equals(condition.getValue());
            case "neq":
                return !propertyValue.equals(condition.getValue());
            case "gt":
                return Double.compare((Double) condition.getValue(), (Double) propertyValue) > 0;
            case "lt":
                return Double.compare((Double) condition.getValue(), (Double) propertyValue) < 0;
            case "gte":
                return Double.compare((Double) condition.getValue(), (Double) propertyValue) >= 0;
            case "lte":
                return Double.compare((Double) condition.getValue(), (Double) propertyValue) <= 0;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + condition.getOperator());
        }
    }

    public boolean evaluateCondition(ApplianceRule.Condition condition, ApplianceState applianceState) {
        String property = condition.getProperty();
        Object value = condition.getValue();
        String operator = condition.getOperator();

        Object stateValue = applianceState.getProperties().getReported().getProperties().get(property);

        if (stateValue == null) {
            return false;
        }


        switch (operator) {
            case "eq":
                return stateValue.equals(value);
            case "neq":
                return !stateValue.equals(value);
            case "gt":
                return Double.compare((Double) stateValue, (Double) value) > 0;
            case "lt":
                return Double.compare((Double) stateValue, (Double) value) < 0;
            case "gte":
                return Double.compare((Double) stateValue, (Double) value) >= 0;
            case "lte":
                return Double.compare((Double) stateValue, (Double) value) <= 0;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

    }
}
