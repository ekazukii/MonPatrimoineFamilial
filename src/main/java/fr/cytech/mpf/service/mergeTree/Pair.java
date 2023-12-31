package fr.cytech.mpf.service.mergeTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Une classe simple pour représenter une paire d'objets
public class Pair<T1, T2> {
    
    private T1 first;
    private T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }

    
    public static <T> List<Pair<T, T>> findPairs(List<T> list1, List<T> list2) {

        List<Pair<T, T>> pairs = new ArrayList<>();

        // Si les deux listes sont null, retourner la liste vide
        if (list1 == null && list2 == null) {
            return pairs;
        }

        // Si l'une des listes est null, ajouter des paires avec null pour la liste manquante
        if (list1 == null) {
            for (T item2 : list2) {
                pairs.add(new Pair<>(null, item2));
            }
            return pairs;
        }

        if (list2 == null) {
            for (T item1 : list1) {
                pairs.add(new Pair<>(item1, null));
            }
            return pairs;
        }

        Set<T> matchedItemsList2 = new HashSet<>();

        // Trouver des paires identiques et ajouter à la liste des paires
        for (T item1 : list1) {
            boolean isMatched = false;
            for (T item2 : list2) {
                if (item1.equals(item2)) { // Remplacer areNodesEqual par equals pour les objets génériques
                    pairs.add(new Pair<>(item1, item2));
                    matchedItemsList2.add(item2);
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) { // S'il n'y a pas de correspondance pour item1
                pairs.add(new Pair<>(item1, null));
            }
        }

        // Pour les éléments de la liste 2 qui n'ont pas de correspondance dans la liste 1
        for (T item2 : list2) {
            if (!matchedItemsList2.contains(item2)) {
                pairs.add(new Pair<>(null, item2));
            }
        }

        return pairs;
    }
    
}