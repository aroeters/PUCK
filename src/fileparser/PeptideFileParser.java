/*
 *Author: Arne Roeters
 */
package fileparser;

import collectionobject.PeptideCollection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author arne
 */
public class PeptideFileParser {

    /**
     * Parses the file that contains specific peptides.
     *
     * @param filePath path to file that contains the peptides
     * @return PeptideCollection with all peptides in the file
     * @throws FileNotFoundException if the file is not found
     * @throws IOException when an input or output error occurs
     */
    public final PeptideCollection getPeptideCollectionByFile(final String filePath) throws FileNotFoundException, IOException {
        System.out.println("Collecting peptides from the given peptide file. (-p, --peptide_file)");
        File file2Parse = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file2Parse.getPath()));
        String line;
        PeptideCollection pepCol = new PeptideCollection();
        while ((line = br.readLine()) != null) {
            pepCol.addPeptide(line.replaceAll("\\s", ""));
        }
        System.out.println("Done...");
        return pepCol;
    }

    /**
     * Parses the file that contains specific peptides.
     *
     * @param peptides path to file that contains the peptides
     * @return PeptideCollection with all peptides in the file
     * @throws FileNotFoundException if the file is not found
     * @throws IOException when an input or output error occurs
     */
    public final PeptideCollection getPeptideCollectionByInput(final String peptides) throws FileNotFoundException, IOException {
        System.out.println("Collecting peptides from the commandline. (-g, --peptides)");
        PeptideCollection pepCol = new PeptideCollection();
        String[] peptideArray = peptides.split("_");
        for (String peptide : peptideArray) {
            pepCol.addPeptide(peptide);
        }
        System.out.println("Done...");
        return pepCol;
    }
}
