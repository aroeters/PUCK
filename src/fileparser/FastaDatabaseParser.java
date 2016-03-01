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
public class FastaDatabaseParser {

    /**
     * The path and filename that should be parsed.
     */
    private final String databaseFile;
    /**
     * Contains the type of digestion wanted for the digestion.
     */
    private final Digester digesterType;
    /**
     * The path and filename for the result file.
     */
    private final String resultFileName;
    /**
     * The peptide collection that will hold all peptides.
     */
    private PeptideCollection peptideCollection = new PeptideCollection();
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
     * @param maxProteinNr maximum number of protein that can match to a protein
     * @param resultFile path and filename for the result file
     * @param database String of the database fastaFile
     * @param digestionType type of digestion wanted
     * @param minPepLen minimal length of the peptides to be used
     * @throws IOException when directory or files are not found
     */
    public FastaDatabaseParser(final String coreNumber, final Integer maxProteinNr, String resultFile,
            final String database, final String digestionType, final Integer minPepLen)
            throws IOException, Exception {
        this.databaseFile = database;
        this.digesterType = getDigester(digestionType, minPepLen);
        if (!resultFile.endsWith("/")) {
            resultFile = resultFile + "/";
        }
        String filename = database.split("/")[database.split("/").length - 1];
        this.resultFileName = resultFile + filename.replace(".fasta", "");
        System.out.println("The final results will be placed in: " + resultFile);
        this.coreNr = coreNumber;
        this.maxProteins = maxProteinNr;
    }

    /**
     * Reads the file and parses it.
     *
     * @return ProteinCollection with all proteins in it
     * @throws FileNotFoundException if the file not exists
     * @throws IOException .
     */
    public final ProteinCollection getProteinCollection()
            throws IOException, FileNotFoundException, Exception {
        ProteinCollection pc = new ProteinCollection();
        File file2Parse = new File(this.databaseFile);
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
                            protein.addTotalPeptides(peptideCollection.addPeptide(peptide));
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
        System.out.println("Finished collecting proteins from: " + this.databaseFile);
        return pc;
    }

    /**
     * writes the uniqueness of the peptide per protein to a file.
     *
     * @param proteinCollection a filled protein collection
     * @throws java.io.IOException when input or output gives an error
     * @throws Exception when an error occurs
     */
    public final void getPeptideUniqueness(final ProteinCollection proteinCollection) throws IOException, Exception {
        ENSEMBLDatabaseConnector edc = new ENSEMBLDatabaseConnector();
        long start = System.currentTimeMillis();
        // Get all matches per peptide with the given database
        CallablePeptideMatcher searcher = new CallablePeptideMatcher(peptideCollection, proteinCollection);
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
                Integer peptideIndex = peptideCollection.getPeptideIndex(futureSet.get(0));
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
        CsvFileWriter cfw = new CsvFileWriter(this.resultFileName, peptideCollection, Integer.parseInt(this.coreNr));
        // Write all results to the files
        cfw.writeExcessPeptides(excessivePeptides);
        cfw.writeGeneUniquenessToCsv(geneCol);
        cfw.writeProteinUniquenessToCsv(proteinCollection);
    }
    /**
     * Get the digester method to use.
     * @param digestionType method for digesting the peptides
     * @param minPepLen minimal peptide length
     * @return the Digester
     */
    private Digester getDigester(final String digestionType, final Integer minPepLen) {
        switch (digestionType) {
            case "0": {
                return new NoDigester(minPepLen);
            }
            case "1": {
                return new TrypsinDigesterConservative(minPepLen);
            }
            case "2": {
                return new TrypsinDigester(minPepLen);
            }
            case "3": {
                return new PepsinDigesterHigherPH(minPepLen);
            }
            case "4": {
                return new PepsinDigesterLowPH(minPepLen);
            }
            case "5": {
                return new ChemotrypsinDigesterHighSpecific(minPepLen);
            }
            case "6": {
                return new ChemotrypsinDigesterLowSpecific(minPepLen);
            }
            default: {
                return new NoDigester(minPepLen);
            }
        }
    }
}