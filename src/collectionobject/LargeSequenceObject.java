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
public interface LargeSequenceObject {
    /**
     * Returns the AA sequence of the protein.
     *
     * @return String AA sequence
     */
    String getSequence();
    /**
     * Sets the AA sequence of the protein.
     *
     * @param newSequence String AA sequence
     */
    void setSequence(final String newSequence);
    /**
     * Gets the name of the object.
     *
     * @return String object name
     */
    String getName();

    /**
     * Sets the name of the object.
     *
     * @param newName name of the object
     */
    void setName(final String newName);

    /**
     * Returns the whole ArrayList of unique peptides.
     *
     * @return ArrayList< String >
     */
    ArrayList<Integer> getUniquePeptides();

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    void addUniquePeptide(final Integer peptidePosition);

    /**
     * Returns the whole ArrayList of non unique peptides.
     *
     * @return ArrayList< String >
     */
    ArrayList<Integer> getNonUniquePeptides();

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    void addNonUniquePeptide(final Integer peptidePosition);

    /**
     * checks if the peptide is part of the gene.
     *
     * @param peptide String of the peptide
     * @return True if peptide is part of the gene
     */
    Boolean checkTotalPeptide(final Integer peptide);

    /**
     * Adds to the total list of peptides.
     *
     * @param peptides a set of peptides that are part of the gene.
     */
    void addTotalPeptides(final HashSet<Integer> peptides);
    /**
     * Adds to the total list of peptides.
     *
     * @param peptide a single peptide that is part of the gene.
     */
    void addTotalPeptides(final Integer peptide);

    /**
     * Returns all peptides indexes from the protein.
     *
     * @return HashSet all_peptides
     */
    HashSet<Integer> getTotalPeptides();
}
