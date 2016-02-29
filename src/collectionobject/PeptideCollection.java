/*
 * Author: Arne Roeters
 */
package collectionobject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        return (String) new ArrayList(peptides.keySet()).get(peptidePosition);

    }
}
