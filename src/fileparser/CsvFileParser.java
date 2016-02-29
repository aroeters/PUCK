///*
// * Author: Arne Roeters
// */
//package fileparser;
//
//import PeptideDigestors.*;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import filewriter.CsvFileWriter;
//import object.PeptideCollection;
//import object.Protein;
//import object.ProteinCollection;
//
///**
// *
// * @author arne
// */
//public class CsvFileParser {
//
//    /**
//     * String with the path and name to the protein-peptides file.
//     */
//    private final String peptides;
//    /**
//     * String with the path to the directory for the results
//     */
//    private final String resultDirectory;
//
//    /**
//     * The Constructor of the class.
//     *
//     * @param peptideFile the protein-peptides file
//     * @param resultDir The directory for the result files
//     */
//    public CsvFileParser(final String peptideFile, final String resultDir) {
//        this.peptides = peptideFile;
//        String sampleId = peptideFile.split("/")[peptideFile.split("/").length - 2];
//        if (resultDir.endsWith("/")) {
//            this.resultDirectory = resultDir + sampleId;
//        } else {
//            this.resultDirectory = resultDir + "/" + sampleId;
//        }
//    }
//
//    /**
//     * parses the protein-peptides.csv file for protein uniqueness.
//     *
//     * @param fastaFile the file that contains the full protein sequences to
//     * check against
//     * @param ensgDatabase the ENST to ENSG conversion file
//     * @param digestion type of digestion wanted
//     * @param minPepLen Minimal length for a peptide
//     * @throws FileNotFoundException If the Fasta file is not found
//     * @throws IOException if the in or output is not correct
//     */
//    public final void parseProteinPeptides(final String fastaFile,
//            final String ensgDatabase,
//            final String digestion,
//            final Integer minPepLen)
//            throws FileNotFoundException, IOException {
//
//        FastaFileParser ffp = new FastaFileParser(fastaFile);
//        ProteinCollection pc = ffp.readFile(ensgDatabase);
//        HashMap<String, Protein> proteins = pc.getProteins();
//        Digestor digestor;
//        switch (digestion) {
//            case "0": {
//                digestor = new NoDigestor(minPepLen);
//                break;
//            }
//            case "1": {
//                digestor = new TrypsinDigestorConservative(minPepLen);
//                break;
//            }
//            case "2": {
//                digestor = new TrypsinDigestor(minPepLen);
//                break;
//            }
//            case "3": {
//                digestor = new PepsinDigestorHigherPH(minPepLen);
//                break;
//            }
//            case "4": {
//                digestor = new PepsinDigestorLowPH(minPepLen);
//                break;
//            }
//            case "5": {
//                digestor = new ChemotrypsinDigestorHighSpecific(minPepLen);
//                break;
//            }
//            case "6": {
//                digestor = new ChemotrypsinDigestorLowSpecific(minPepLen);
//                break;
//            }
//            default: {
//                digestor = new NoDigestor(minPepLen);
//                break;
//            }
//        }
//        long start = System.currentTimeMillis();
//        File file = new File(this.peptides);
//        if (file.isFile()) {
//            BufferedReader br = new BufferedReader(
//                    new FileReader(file.getPath()));
//            String line;
//            Boolean firstLine = true;
//            String peptideSequence;
//            PeptideCollection pepCol = new PeptideCollection();
//            ArrayList<String> digestedPeptide = null;
//            ArrayList<String> matchedProteins;
//            String name;
//            Protein prot;
//            ArrayList<String> geneNames;
//            Integer count = 0;
//            while ((line = br.readLine()) != null) {
//                count++;
//                geneNames = new ArrayList<>();
//                if (firstLine) {
//                    firstLine = false;
//                } else {
//                    if (line.contains(",")) {
//                        String[] splitLine = line.split(",");
//                        if (splitLine.length > 3) {
//                            peptideSequence = line.split(",")[3].replaceAll("\\(.+?\\)", "");
//                            if (peptideSequence.split("\\.").length >= 2) {
//                                digestedPeptide = digestor.digest(peptideSequence.split("\\.")[1]);
//                            }
//                            for (String peptide : digestedPeptide) {
//                                Integer position = pepCol.addPeptide(peptide);
//                                if (position != null) {
//                                    matchedProteins = new ArrayList<>();
//                                    for (Protein protein : proteins.values()) {
//                                        // If the peptide sequence is in the protein sequence.
//                                        if (protein.getSequence().contains(peptide)) {
//                                            matchedProteins.add(protein.getName());
//                                        }
//                                    }
//                                    if (matchedProteins.size() == 1) {
//                                        proteins.get(matchedProteins.get(0))
//                                                .addUniquePeptide(position);
//                                    } else {
//                                        for (String matchedProtein : matchedProteins) {
//                                            proteins.get(matchedProtein)
//                                                    .addNonUniquePeptide(position);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            System.out.println(System.currentTimeMillis() - start + " m/s");
//            CsvFileWriter cfw = new CsvFileWriter(this.resultDirectory);
//            cfw.writeProteinUniquenessToCsv(pc, pepCol.getAllPeptides());
//        }
//    }
//}
