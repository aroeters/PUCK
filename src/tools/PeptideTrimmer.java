/*
 *Author: Arne Roeters
 */
package tools;

/**
 *
 * @author arne
 */
public class PeptideTrimmer {
    /**
     * Trims the peptide if it contains the AA before and after
     * @param peptideSequence the sequence with the amino acid before and after the peptide
     * @return 
     */
    public final String trimPeptide(final String peptideSequence) {
        if (peptideSequence.indexOf(".") == 1) {
            return peptideSequence.split("\\.")[1];
        } else if (peptideSequence.contains(".")) {
            return peptideSequence.split("\\.")[0];
        } else {
           return peptideSequence;
        }
    }
}
