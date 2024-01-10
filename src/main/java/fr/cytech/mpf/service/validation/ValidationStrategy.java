package fr.cytech.mpf.service.validation;

import java.util.List;

import fr.cytech.mpf.entity.Node;

public class ValidationStrategy {
    private List<ValidationRule> rules;

    public ValidationStrategy(List<ValidationRule> rules) {
        this.rules = rules;
    }

    public ValidationResponse validate(Node node) {
        ValidationResponse response = new ValidationResponse();
        for (ValidationRule rule : rules) {
            if (!rule.validate(node)) {
                response.setError(rule.getErrorMessage());
                return response;
            }
        }
        response.setSuccess();
        return response;
    }
}
