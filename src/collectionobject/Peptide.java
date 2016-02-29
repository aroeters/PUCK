/*
 * Author: Arne Roeters
 */
package collectionobject;

import java.util.ArrayList;

/**
 * @author arne
 */
public class Peptide {

    /**
     * The amino acid sequence of the peptide.
     */
    private String sequence;
    /**
     * The sample the peptide is found in.
     */
    private String sampleName;
    /**
     * The gene that the peptide is coming from.
     */
    private final ArrayList<String> genes = new ArrayList<>();

    /**
     * Returns the peptide sequence.
     *
     * @return String peptide sequence
     */
    public final String getSequence() {
        return sequence;
    }

    /**
     * Sets the sequence of the peptide object.
     *
     * @param newSequence String sequence
     */
    public final void setSequence(final String newSequence) {
        this.sequence = newSequence;
    }

    /**
     * Returns the name of the sample where the peptide is found.
     *
     * @return String sampleName
     */
    public final String getSampleName() {
        return sampleName;
    }

    /**
     * Sets the name of the sample in the peptide object.
     *
     * @param newSampleName String name of the sample
     */
    public final void setSampleName(final String newSampleName) {
        this.sampleName = newSampleName;
    }

    /**
     * Returns the name of the gene where the peptide is coming from.
     *
     * @return String geneName
     */
    public final ArrayList<String> getGenes() {
        return genes;
    }

    /**
     * Sets the name of the gene where the peptide is coming from.
     *
     * @param newGene String gene name
     */
    public final void addGene(final String newGene) {
        if (!this.genes.contains(newGene)) {
            this.genes.add(newGene);
        }
    }

    /**
     * Returns the number of genes that the peptide is coming from.
     *
     * @return Integer number of genes
     */
    public final Integer numberOfGenes() {
        return this.genes.size();
    }
}
