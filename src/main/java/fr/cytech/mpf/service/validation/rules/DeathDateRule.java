package fr.cytech.mpf.service.validation.rules;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.service.validation.ValidationRule;

public class DeathDateRule implements ValidationRule {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean validate(Node node) {
        return (DeathDateRule.isDeathDateValid(node.getParentA().getBirthDate(), node.getBirthDate())
            && DeathDateRule.isDeathDateValid(node.getParentB().getBirthDate(), node.getBirthDate()));
    }

    @Override
    public String getErrorMessage() {
        return "Erreur de validation de la date de décès"; 
    }

    private static boolean isDeathDateValid(String deathDateParent, String birthDateChild) {
        LocalDate dateOfDeathParent = LocalDate.parse(deathDateParent, FORMATTER);
        LocalDate dateOfBirthChild = LocalDate.parse(birthDateChild, FORMATTER);

        // Vérification si la date de décès du parent est après ou égale à la date de naissance de l'enfant
        return !dateOfDeathParent.isBefore(dateOfBirthChild);
    }
}