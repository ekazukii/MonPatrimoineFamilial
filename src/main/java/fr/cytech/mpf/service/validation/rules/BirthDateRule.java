package fr.cytech.mpf.service.validation.rules;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.service.validation.ValidationRule;

public class BirthDateRule implements ValidationRule {

    private static final int MIN_AGE_DIFFERENCE = 10;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean validate(Node node) {

        if (node.getFather() != null && !BirthDateRule.isOldEnough(node.getFather().getBirthDate(), node.getBirthDate())) {
            return false;
        }
    
        if (node.getMother() != null && !BirthDateRule.isOldEnough(node.getMother().getBirthDate(), node.getBirthDate())) {
            return false;
        }
    
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Erreur de validation de la date de naissance"; // Modifiez pour un message d'erreur approprié
    }

    private static boolean isOldEnough(String birthDateParent, String birthDateChild) {
        // Conversion des chaînes de dates de naissance en objets LocalDate
        LocalDate dateOfBirthParent = LocalDate.parse(birthDateParent, FORMATTER);
        LocalDate dateOfBirthChild = LocalDate.parse(birthDateChild, FORMATTER);

        // Calcul de la date de naissance minimum que doit avoir le parent
        LocalDate minimumBirthDateForParent = dateOfBirthChild.minusYears(MIN_AGE_DIFFERENCE);

        // Vérification si le parent est assez âgé
        return dateOfBirthParent.isBefore(minimumBirthDateForParent) || dateOfBirthParent.isEqual(minimumBirthDateForParent);
    }
}