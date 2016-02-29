/*
 * Author: Arne Roeters
 */
package collectionobject;

/**
 * @author arne
 */
public class Peptide {

    /**
     * The amino acid sequence of the peptide.
     */
    private String sequence;

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
}
