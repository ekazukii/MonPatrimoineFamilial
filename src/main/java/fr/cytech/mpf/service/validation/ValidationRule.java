package fr.cytech.mpf.service.validation;

import fr.cytech.mpf.entity.Node;

public interface ValidationRule {
    boolean validate(Node node);
    String getErrorMessage();
}