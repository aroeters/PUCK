/*
 *Author: Arne Roeters
 */
package collectionobject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author arne
 */
public class Protein {

    /**
     * The name of the protein.
     */
    private String name;
    /**
     * The Amino Acid sequence of the protein.
     */
    private String sequence;
    /**
     * Contains a list of all peptides of the protein.
     */
    private final HashSet<Integer> all_peptides = new HashSet<>();
    /**
     * A list of unique peptides.
     */
    private final ArrayList<Integer> unique_peptides = new ArrayList<>();
    /**
     * A list of non unique peptides.
     */
    private final ArrayList<Integer> peptides = new ArrayList<>();

    /**
     * Returns the name of the Protein.
     *
     * @return String name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Sets the name of the protein.
     *
     * @param newName the name of the protein
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Returns the AA sequence of the protein.
     *
     * @return String AA sequence
     */
    public final String getSequence() {
        return this.sequence;
    }

    /**
     * Sets the AA sequence of the protein.
     *
     * @param newSequence String AA sequence
     */
    public final void setSequence(final String newSequence) {
        if (this.sequence == null) {
            this.sequence = newSequence;
        } else {
            this.sequence += newSequence.replaceAll("[\n]\\s+", "");
        }
    }

    /**
     * Returns the whole ArrayList of unique peptides.
     *
     * @return ArrayList< String >
     */
    public final ArrayList<Integer> getUniquePeptides() {
        return unique_peptides;
    }

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    public final void addUniquePeptide(final Integer peptidePosition) {
        unique_peptides.add(peptidePosition);
    }

    /**
     * Adds to the total list of peptides.
     *
     * @param peptide the peptide sequence
     */
    public final void addTotalPeptides(final Integer peptide) {
        this.all_peptides.add(peptide);
    }

    /**
     * Returns all peptides indexes from the protein.
     *
     * @return HashSet<Integer> all_peptides
     */
    public final HashSet<Integer> getTotalPeptides() {
        return all_peptides;
    }

    /**
     * Returns the whole ArrayList of non unique peptides.
     *
     * @return ArrayList< String >
     */
    public final ArrayList<Integer> getNonUniquePeptides() {
        return peptides;
    }

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    public final void addNonUniquePeptide(final Integer peptidePosition) {
        peptides.add(peptidePosition);
    }

    /**
     * checks if the peptide is part of the protein.
     *
     * @param peptide String of the peptide
     * @return True if peptide is part of the protein
     */
    public final Boolean checkPeptide(final String peptide) {
        return this.sequence.contains(peptide);
    }
    /**
     * checks if the peptide is part of the protein.
     *
     * @param peptide String of the peptide
     * @return True if peptide is part of the protein
     */
    public final Boolean checkTotalPeptide(final Integer peptide) {
        return this.all_peptides.contains(peptide);
    }
}
