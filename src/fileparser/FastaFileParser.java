/*
 *Author: Arne Roeters
 */
package fileparser;

import callable.CallablePeptideMatcher;
import databaseconnection.ENSEMBLDatabaseConnector;
import filewriter.CsvFileWriter;
import peptidedigesters.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Future;
import collectionobject.Gene;
import collectionobject.GeneCollection;
import collectionobject.PeptideCollection;
import collectionobject.Protein;
import collectionobject.ProteinCollection;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author arne
 */
public class FastaFileParser {

    /**
     * The path and filename that should be parsed.
     */
    private final String file;
    /**
     * Contains the type of digestion wanted for the digestion.
     */
    private final Digester digesterType;
    /**
     * The path and filename for the result fiHashSet<String>le.
     */
    private final String resultFileName;
    /**
     * The protein collection to use.
     */
    private ProteinCollection proteinCollection;

    /**
     * The number of cores to use.
     */
    private final String coreNr;
    /**
     * the maximum number of proteins a peptide can match to and still be taken
     * into account.
     */
    private final Integer maxProteins;

    /**
     * Constructor of the class.
     *
     * @param coreNumber number of cores to use
     * @param resultFile path and filename for the result file
     * @param database String of the database fastaFile
     * @param digestiontype type of digestion wanted
     * @param minPepLen minimal length of the peptides to be used
     * @throws IOException when directory or files are not found
     */
    public FastaFileParser(final String coreNumber, final Integer maxProteinNr, String resultFile,
            final String database, final String digestiontype, final Integer minPepLen)
            throws IOException, Exception {
        Digester digester;
        switch (digestiontype) {
            case "0": {
                digester = new NoDigester(minPepLen);
                break;
            }
            case "1": {
                digester = new TrypsinDigesterConservative(minPepLen);
                break;
            }
            case "2": {
                digester = new TrypsinDigester(minPepLen);
                break;
            }
            case "3": {
                digester = new PepsinDigesterHigherPH(minPepLen);
                break;
            }
            case "4": {
                digester = new PepsinDigesterLowPH(minPepLen);
                break;
            }
            case "5": {
                digester = new ChemotrypsinDigesterHighSpecific(minPepLen);
                break;
            }
            case "6": {
                digester = new ChemotrypsinDigesterLowSpecific(minPepLen);
                break;
            }
            default: {
                digester = new NoDigester(minPepLen);
                break;
            }
        }
        this.file = database;
        String filename = database.split("/")[database.split("/").length - 1];
        this.digesterType = digester;
        if (!resultFile.endsWith("/")) {
            resultFile = resultFile + "/";
        }
        this.resultFileName = resultFile + filename.replace(".fasta", "");
        System.out.println("The final results will be placed in: " + resultFile);
        this.coreNr = coreNumber;
        this.maxProteins = maxProteinNr;
        readFile(database);
    }

    /**
     * Reads the file and parses it.
     *
     * @param ensgDatabase ENST/UniProt ID to ENSG conversion database
     * @throws FileNotFoundException if the file not exists
     * @throws IOException .
     */
    public final void readFile(final String ensgDatabase)
            throws IOException, FileNotFoundException, Exception {
        ProteinCollection pc = new ProteinCollection();
        PeptideCollection pepCol = new PeptideCollection();
        File file2Parse = new File(this.file);
        BufferedReader br = new BufferedReader(new FileReader(file2Parse.getPath()));
        String line;
        String name = "";
        Protein protein = new Protein();
        Boolean notValidProtein = false;

        while ((line = br.readLine()) != null) {
            if (!notValidProtein) {
                if (line.startsWith(">NEWP") || line.startsWith(">GENSCAN")) {
                    notValidProtein = true;
                } else if (line.startsWith(">") && !pc.getProteinNames().contains(line.substring(1))) {
                    if (protein.getName() != null) {
                        ArrayList<String> peptides = this.digesterType.digest(protein.getSequence());
                        for (String peptide : peptides) {
                            protein.addTotalPeptides(pepCol.addPeptide(peptide));
                        }
                        pc.addProtein(name, protein);
                        protein = new Protein();
                    }
                    if (line.startsWith(">sp") || line.startsWith(">tr")) {
                        name = line.split("\\|")[1];
                    } else {
                        name = line.substring(1).replaceAll("\\s", "");
                    }
                    protein.setName(name);
                } else {
                    protein.setSequence(line);
                }
            } else if (notValidProtein) {
                notValidProtein = false;
            }
        }
        System.out.println("Finished collecting proteins from: " + this.file);
        getPeptideUniqueness(pc, pepCol);
    }

    /**
     * writes the uniqueness of the peptide per protein to a file.
     *
     * @param proteinCollection a filled protein collection
     * @param pepCol a filled peptide collection
     * @throws java.io.IOException when input or output gives an error
     * @throws Exception when an error occurs
     */
    public final void getPeptideUniqueness(final ProteinCollection proteinCollection, final PeptideCollection pepCol) throws IOException, Exception {
        ENSEMBLDatabaseConnector edc = new ENSEMBLDatabaseConnector();
        long start = System.currentTimeMillis();
        // Get all matches per peptide with the given database
        CallablePeptideMatcher searcher = new CallablePeptideMatcher(pepCol, proteinCollection);
        Set<Future<LinkedList<String>>> set = searcher.matchPeptides(Integer.parseInt(this.coreNr));
        // create a geneCollection which will hold the genes that are needed for the output
        GeneCollection geneCol = new GeneCollection();
        HashMap<String, ArrayList<String>> geneMap;
        LinkedList<String> futureSet;
        Gene gene;
        HashMap<String, Integer> excessivePeptides = new HashMap<>();
        for (Future<LinkedList<String>> future : set) {
            futureSet = future.get();
            if (futureSet.size() > this.maxProteins) {
                excessivePeptides.put(futureSet.get(0), futureSet.size());
            } else {
                Integer peptideIndex = pepCol.getPeptideIndex(futureSet.get(0));
                geneMap = new HashMap<>();
                String proteinNameConverted;
                ArrayList<String> proteinList;
                for (String proteinName : futureSet.subList(1, futureSet.size())) {
                    proteinNameConverted = edc.getENSG(proteinName);
                    if (geneMap.containsKey(proteinNameConverted)) {
                        geneMap.get(proteinNameConverted).add(proteinName);
                    } else {
                        proteinList = new ArrayList<>(Arrays.asList(proteinName));
                        geneMap.put(proteinNameConverted, proteinList);
                    }
                }
                for (String geneName : geneMap.keySet()) {
                    if (geneMap.size() == 1) {
                        if (geneCol.checkGene(geneName)) {
                            gene = geneCol.getGene(geneName);
                            gene.addUniquePeptide(peptideIndex);
                            for (String proteinName : geneMap.get(geneName)) {
                                gene.addTotalPeptides(proteinCollection.getProtein(proteinName).getTotalPeptides());
                            }
                        } else {
                            gene = new Gene(geneName);
                            gene.addUniquePeptide(peptideIndex);
                            for (String proteinName : geneMap.get(geneName)) {
                                gene.addTotalPeptides(proteinCollection.getProtein(proteinName).getTotalPeptides());
                            }
                            geneCol.addGene(geneName, gene);
                        }
                    } else {
                        if (geneCol.checkGene(geneName)) {
                            gene = geneCol.getGene(geneName);
                            gene.addNonUniquePeptide(peptideIndex);
                            for (String proteinName : geneMap.get(geneName)) {
                                gene.addTotalPeptides(proteinCollection.getProtein(proteinName).getTotalPeptides());
                            }
                        } else {
                            gene = new Gene(geneName);
                            gene.addNonUniquePeptide(peptideIndex);
                            for (String proteinName : geneMap.get(geneName)) {
                                gene.addTotalPeptides(proteinCollection.getProtein(proteinName).getTotalPeptides());
                            }
                            geneCol.addGene(geneName, gene);
                        }
                    }

                }
                if (futureSet.size() == 2) {
                    proteinCollection.getProtein(futureSet.get(1)).addUniquePeptide(peptideIndex);
                } else {
                    for (int i = 1; i < futureSet.size(); i++) {
                        proteinCollection.getProtein(futureSet.get(i)).addNonUniquePeptide(peptideIndex);
                    }
                }
            }
        }
        long end = System.currentTimeMillis() - start;
        System.out.println(end + "ms = " + end / 1000 + "s = " + end / 60000 + "m = " + end / 3600000 + "h");
        CsvFileWriter cfw = new CsvFileWriter(this.resultFileName, pepCol, Integer.parseInt(this.coreNr));
        cfw.writeGeneUniquenessToCsv(geneCol);
        cfw.writeProteinUniquenessToCsv(proteinCollection);
        cfw.writeExcessPeptides(excessivePeptides);
    }
}
