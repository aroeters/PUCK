/*
 * Author: Arne Roeters
 */
package collectionobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author arne
 */
public class PeptideCollection {

    /**
     * The collection of all peptides.
     */
    private final LinkedHashMap<String, Integer> peptides = new LinkedHashMap<>();
    /**
     * LinkedList that holds all the keys of the HashMap
     */
    LinkedList<String> LinkedHashMapKeys = new LinkedList<>();
    /**
     * Number of peptides in the HashMap peptides.
     */
    private Integer peptideNr = 0;

    /**
     * Returns the complete HashMap with key the ENSEMBL entry and value peptide
     * Object.
     *
     * @return HashMap key:ENSEMBL entry Value:Peptide Object
     */
    public final ArrayList<String> getAllPeptides() {
        return new ArrayList(peptides.keySet());
    }

    /**
     * Adds a Peptide Object to the LinkedList.
     *
     * @param peptide peptide sequence
     * @return index of the peptide
     */
    public final Integer addPeptide(final String peptide) {
        if (!peptides.containsKey(peptide)) {
            this.peptides.put(peptide, peptideNr);
            this.LinkedHashMapKeys.add(peptide);
            peptideNr++;
            return peptideNr;
        }
        return getPeptideIndex(peptide);
    }

    public final Integer getPeptideIndex(final String peptideSequence) {
        return peptides.get(peptideSequence);
    }

    /**
     * Returns the peptide given the peptide position.
     *
     * @param peptidePosition the sequence of the peptide in String format
     * @return String of the peptideSequence
     */
    public final String getPeptideSequence(final Integer peptidePosition) {
        return this.LinkedHashMapKeys.get(peptidePosition);
    }

    /**
     * Returns every peptide String in the list.
     *
     * @param indices all indices of the peptide.
     * @return HashSet of peptides
     */
    public final HashSet<String> getAllPeptideSequences(final HashSet<Integer> indices) {
        HashSet<String> peptideSet = new HashSet<>();
        for (Integer i : indices) {
            peptideSet.add(getPeptideSequence(i));
        }
        return peptideSet;
    }
}
