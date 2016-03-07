/*
 *Author: Arne Roeters
 */
package filewriter;

import callable.CallableUniquessChecker;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import collectionobject.GeneCollection;
import collectionobject.PeptideCollection;
import collectionobject.ProteinCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Future;

/**
 *
 * @author arne
 */
public class CsvFileWriter {

    /**
     * The directory where the file is written to.
     */
    private final String dir;
    /**
     * Contains all matched peptides.
     */
    private final PeptideCollection pepCol;
    /**
     * The number of cores to use.
     */
    private final Integer threadNr;
    /**
     * The PeptideCollection created by the digestion.
     */
    private PeptideCollection digestPeptides = null;

    /**
     * Constructor of the class.
     *
     * @param directory where the file is written to.
     * @param peptideCollection the peptideCollection
     * @param thread the number of threads to use
     * @param digestionPeptides the peptides created by digestion
     */
    public CsvFileWriter(final String directory, final PeptideCollection peptideCollection, final Integer thread,
            final PeptideCollection digestionPeptides) {
        this.dir = directory;
        this.pepCol = peptideCollection;
        this.threadNr = thread;
        this.digestPeptides = digestionPeptides;
    }

    /**
     * Constructor of the class.
     *
     * @param directory where the file is written to.
     * @param peptideCollection the peptideCollection
     * @param thread the number of threads to use
     */
    public CsvFileWriter(final String directory, final PeptideCollection peptideCollection, final Integer thread) {
        this.dir = directory;
        this.pepCol = peptideCollection;
        this.threadNr = thread;
    }

    /**
     * Writes all the gene uniqueness results to a csv file.
     *
     * @param geneCol the collection of genes
     * @throws FileNotFoundException when the file is not found
     * @throws UnsupportedEncodingException when the encoding is not supported
     * @throws IOException when something goes wrong with I/O
     * @throws Exception when an error occurs
     */
    public final void writeGeneUniquenessToCsv(final GeneCollection geneCol)
            throws FileNotFoundException, UnsupportedEncodingException, IOException, Exception {
        System.out.println("writing genes to file");
        File file = new File(this.dir + "_geneUniqueness.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Gene_name\tunique_peptides\tnon_unique_peptides\n");
            CallableUniquessChecker searcher;
            if (digestPeptides == null) {
                searcher = new CallableUniquessChecker(pepCol, geneCol);
            } else {
                searcher = new CallableUniquessChecker(pepCol, geneCol, digestPeptides);
            }
            Set<Future<HashMap<String, ArrayList<String>>>> set = searcher.checkPeptideUniqueness(this.threadNr);
            ArrayList<String> temporary;
            for (Future<HashMap<String, ArrayList<String>>> future : set) {
                HashMap<String, ArrayList<String>> futureMap = future.get();
                temporary = new ArrayList<>();
                temporary.addAll(futureMap.keySet());
                for (String name : temporary) {
                    if (!name.equals("unique") && !name.equals("non_unique")) {
                        bw.write(name + "\t");
                    }
                }
                for (String peptide : futureMap.get("unique")) {
                    if (futureMap.get("unique").indexOf(peptide) == futureMap.get("unique").size() - 1) {
                        bw.write(peptide);
                    } else {
                        bw.write(peptide + ";");
                    }
                }
                bw.write("\t");
                for (String peptide : futureMap.get("non_unique")) {
                    if (futureMap.get("non_unique").indexOf(peptide) == futureMap.get("non_unique").size() - 1) {
                        bw.write(peptide);
                    } else {
                        bw.write(peptide + ";");
                    }
                }
                bw.write("\n");
            }
        }
        System.out.println("Done...");
    }

    /**
     * Writes all the protein results to a csv file.
     *
     * @param protCol the collection with proteins
     * @throws IOException when something goes wrong in the I/O
     * @throws Exception when an error occurs
     */
    public final void writeProteinUniquenessToCsv(final ProteinCollection protCol) throws IOException, Exception {
        System.out.println("writing proteins to file");
        File file = new File(this.dir + "_proteinUniqueness.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Protein_name\tunique_peptides\tnon-unique_peptides\n");
            CallableUniquessChecker searcher;
            if (digestPeptides == null) {
                searcher = new CallableUniquessChecker(pepCol, protCol);
            } else {
                searcher = new CallableUniquessChecker(pepCol, protCol, digestPeptides);
            }
            Set<Future<HashMap<String, ArrayList<String>>>> set = searcher.checkPeptideUniqueness(this.threadNr);
            ArrayList<String> temporary;
            for (Future<HashMap<String, ArrayList<String>>> future : set) {
                HashMap<String, ArrayList<String>> futureMap = future.get();
                if (futureMap.get("unique").isEmpty() && futureMap.get("non_unique").isEmpty()) {
                } else {
                    temporary = new ArrayList<>();
                    temporary.addAll(futureMap.keySet());
                    for (String name : temporary) {
                        if (!name.equals("unique") && !name.equals("non_unique")) {
                            bw.write(name + "\t");
                        }
                    }
                    for (String peptide : futureMap.get("unique")) {
                        if (futureMap.get("unique").indexOf(peptide) == futureMap.get("unique").size() - 1) {
                            bw.write(peptide);
                        } else {
                            bw.write(peptide + ";");
                        }
                    }
                    bw.write("\t");
                    for (String peptide : futureMap.get("non_unique")) {
                        if (futureMap.get("non_unique").indexOf(peptide) == futureMap.get("non_unique").size() - 1) {
                            bw.write(peptide);
                        } else {
                            bw.write(peptide + ";");
                        }
                    }
                    bw.write("\n");
                }
            }
        }
        System.out.println("Done...");
    }

    /**
     * Writes all peptides above the threshold of protein matches per peptide to
     * a separate file.
     *
     * @param excessPeptides all excess peptides
     * @throws IOException when an input/output occurs
     */
    public final void writeExcessPeptides(final HashMap<String, Integer> excessPeptides) throws IOException {
        System.out.println("writing excessive matching peptides to file");
        File file = new File(this.dir + "_excessive_peptides.csv");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Peptide\tNr_protein_matches\n");
            for (String peptide : excessPeptides.keySet()) {
                bw.write(peptide + "\t" + excessPeptides.get(peptide) + "\n");
            }
        }
        System.out.println("Done...");
    }
}
