/*
 * Author: Arne Roeters
 */
package proteinquantifier;

import fileparser.FastaFileParser;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.cli.*;

/**
 *
 * @author arne
 */
public class ProteinQuantifier {

    /**
     * Links everything together.
     *
     * @param args command line arguments
     * @throws org.apache.commons.cli.ParseException when the parsing of the
     * arguments gives an exception.
     * @throws java.io.IOException when the I/O is wrong
     * @throws Exception when an error occurs
     */
    public static void main(final String[] args) throws ParseException, IOException, Exception {
        ProteinQuantifier pq = new ProteinQuantifier();
        HashMap cmdArguments = pq.getCMDArguments(args);
        FastaFileParser ffp = new FastaFileParser(
                cmdArguments.get("a").toString(),
                Integer.parseInt(cmdArguments.get("b").toString()),
                cmdArguments.get("c").toString(),
                cmdArguments.get("f").toString(),
                cmdArguments.get("d").toString(),
                Integer.parseInt(cmdArguments.get("e").toString()));
    }

    /**
     * Gets all the arguments from the cmd to know the file locations.
     *
     * @param args cmd arguments
     * @return a HashMap with the flag linked to the argument
     * @throws ParseException when something goes wrong with parsing
     */
    public final HashMap<String, String> getCMDArguments(final String[] args)
            throws ParseException {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("String option")
                      .withDescription("The number of threads to use. (default = 2) (influences processing speed)")
                      .hasOptionalArg()
                      .create("a"));
        options.addOption(OptionBuilder.withLongOpt("String option")
                      .withDescription("The number of protein matches a peptide can have and still be processed. (default = 100) (influences the processing speed)")
                      .hasOptionalArg()
                      .create("b"));
        options.addOption("c", true, "The path to the directory to place the result file in.");
        options.addOption("d", true, "The type of peptide digestion that is wanted:\n"
                + "0:no digestion\n1:Trypsin conservative (only digest at Arg unless Pro after)\n"
                + "2:Trypsin\n3:Pepsin High PH (PH > 2)\n4:Pepsin (PH 1.3)\n"
                + "5:Chemotrypsin high specificity\n6:Chemotrypsin Low specificity");
        options.addOption("e", true, "Minimal length of the peptide to be taken into account");
        options.addOption("f", true, "The path and name of a fasta file that contains the uniprot database");
        options.addOption("g", true, "The path to the directory you want to write the results to");

        HashMap<String, String> arguments = new HashMap<>();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        
        String optionC = Arrays.toString(cmd.getOptionValues("c"));
        String optionD = Arrays.toString(cmd.getOptionValues("d"));
        String optionE = Arrays.toString(cmd.getOptionValues("e"));
        String optionF = Arrays.toString(cmd.getOptionValues("f"));
        String optionG = Arrays.toString(cmd.getOptionValues("g"));
        
        
        String digestion = optionD.substring(1, optionD.length() - 1);
        String cutOff = optionE.substring(1, optionE.length() - 1);
        File peptideFile = new File(optionC.substring(1, optionC.length() - 1));
        File fastaFile = new File(optionF.substring(1, optionF.length() - 1));
        File resultDir = new File(optionG.substring(1, optionG.length() - 1));
        try {
            if (Integer.parseInt(digestion) < 0 || Integer.parseInt(digestion) > 6) {
                System.out.println("The option you chose for digestion is not valid (-d).\n"
                        + "The type of peptide digestion that is wanted:\n"
                        + "0:no digestion\n1:Trypsin conservative (only digest at Arg unless Pro after)\n"
                        + "2:Trypsin\n3:Pepsin High PH (PH > 2)\n4:Pepsin (PH 1.3)\n"
                        + "5:Chemotrypsin high specificity\n6:Chemotrypsin Low specificity\n");
                System.exit(0);
            } else if (Integer.parseInt(cutOff) < 0) {
                System.out.println("Please enter a number of 0 or higher for the cutoff value");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Flag d or Flag e is not a valid number.\nPlease"
                    + "enter a valid number and try again.");
            System.exit(0);
        }
        if (options.getOption("a").getValue() != null) {
            String optionA = Arrays.toString(cmd.getOptionValues("a"));
            String coreNr = optionA.substring(1, optionA.length() - 1);
            try {
                Integer.parseInt(coreNr);
                arguments.put("a", coreNr);
            } catch (IllegalArgumentException e) {
                System.out.println("The maximum number of cores you entered is not a valid number.\n"
                        + "Please try again with a valid number.");
                System.exit(0);
            }
        } else {
            arguments.put("a", "2");
        }
        if (options.getOption("b").getValue() != null) {
            String optionB = Arrays.toString(cmd.getOptionValues("b"));
            String maxProteins = optionB.substring(1, optionB.length() - 1);
            try {
                Integer.parseInt(maxProteins);
                arguments.put("b", maxProteins);
            } catch (IllegalArgumentException e) {
                System.out.println("The maximum number proteins you entered for a peptide is not valid.");
                System.exit(0);
            }
        } else {
            System.out.println("Default for maximum protein matches per peptide is used. (100)");
            arguments.put("b", "100");
        }
        if (!peptideFile.isDirectory()) {
            System.out.println("You should provide a valid path (-c).");
            System.exit(0);
        } else if (!fastaFile.isFile()) {
            System.out.println("The protein database fasta file you provided is not correct. (-f).");
            System.exit(0);
        } else if (!resultDir.isDirectory()) {
            System.out.println("The directory you provided is not a directory (-g).");
            System.exit(0);
        }
        arguments.put("c", peptideFile.toString());
        arguments.put("d", digestion);
        arguments.put("e", cutOff);
        arguments.put("f", fastaFile.toString());
        arguments.put("g", resultDir.toString());
        return arguments;
    }
}
