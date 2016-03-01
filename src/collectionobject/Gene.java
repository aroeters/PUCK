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
public class Gene implements LargeSequenceObject {

    /**
     * String sequence of the gene.
     */
    String sequence;
    /**
     * The name of the Gene.
     */
    private String name;
    /**
     * All Peptides of the gene.
     */
    HashSet<Integer> all_peptides = new HashSet<>();
    /**
     * A list of unique peptides.
     */
    private final ArrayList<Integer> unique_peptides = new ArrayList<>();
    /**
     * A list of non unique peptides.
     */
    private final ArrayList<Integer> peptides = new ArrayList<>();

    /**
     * Returns the name of the Gene.
     *
     * @return String name
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * Two ways of initiating the class. First one without any parameters.
     * Second one with the geneName.
     */
    public Gene() {
    }

    /**
     * Sets the gene name at initiating the Gene object.
     *
     * @param geneName name of the gene
     */
    public Gene(final String geneName) {
        this.name = geneName;
    }

    /**
     * Sets the name of the Gene.
     *
     * @param newName the name of the protein
     */
    @Override
    public final void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Returns the whole ArrayList of unique peptides.
     *
     * @return ArrayList< String >
     */
    @Override
    public final ArrayList<Integer> getUniquePeptides() {
        return unique_peptides;
    }

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    @Override
    public final void addUniquePeptide(final Integer peptidePosition) {
        unique_peptides.add(peptidePosition);
    }

    /**
     * Returns the whole ArrayList of non unique peptides.
     *
     * @return ArrayList< String >
     */
    @Override
    public final ArrayList<Integer> getNonUniquePeptides() {
        return peptides;
    }

    /**
     * Adds a peptide to the unique peptides list.
     *
     * @param peptidePosition the String that contains the sequence of AA's
     */
    @Override
    public final void addNonUniquePeptide(final Integer peptidePosition) {
        peptides.add(peptidePosition);
    }

    /**
     * checks if the peptide is part of the gene.
     *
     * @param peptide String of the peptide
     * @return True if peptide is part of the gene
     */
    @Override
    public final Boolean checkTotalPeptide(final Integer peptide) {
        return this.all_peptides.contains(peptide);
    }

    /**
     * Adds to the total list of peptides.
     *
     * @param peptides a set of peptides that are part of the gene.
     */
    @Override
    public final void addTotalPeptides(final HashSet<Integer> peptides) {
        this.all_peptides.addAll(peptides);
    }

    /**
     * Adds to the total list of peptides.
     *
     * @param peptide a single peptide that is part of the gene.
     */
    @Override
    public final void addTotalPeptides(final Integer peptide) {
        this.all_peptides.add(peptide);
    }

    /**
     * Returns all peptides indexes from the protein.
     *
     * @return HashSet all_peptides
     */
    @Override
    public final HashSet<Integer> getTotalPeptides() {
        return all_peptides;
    }

    /**
     * Returns the AA sequence of the protein.
     *
     * @return String AA sequence
     */
    @Override
    public String getSequence() {
        return this.sequence;
    }

    /**
     * Sets the AA sequence of the protein.
     *
     * @param newSequence String AA sequence
     */
    @Override
    public final void setSequence(final String newSequence) {
        if (this.sequence == null) {
            this.sequence = newSequence;
        } else {
            this.sequence += newSequence.replaceAll("[\n]\\s+", "");
        }
    }
}
