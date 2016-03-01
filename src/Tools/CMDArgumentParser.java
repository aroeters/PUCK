/*
 *Author: Arne Roeters
 */
package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author arne
 */
public class CMDArgumentParser {

    /**
     * Contains Arguments that are given in the CMD.
     */
    private final HashMap<String, String> arguments = new HashMap<>();

    /**
     * Gets all the arguments from the CMD to know the file locations.
     *
     * @param args CMD arguments
     * @return a HashMap with the flag linked to the argument
     * @throws ParseException when something goes wrong with parsing
     * @throws FileNotFoundException if the specified file is not found.
     * @throws IOException when an error occurs in the handling of I/O
     */
    public final HashMap<String, String> getCMDArguments(final String[] args)
            throws ParseException, FileNotFoundException, IOException {
        // Create options object
        Options options = new Options();
        // Add all options
        options.addOption(OptionBuilder.withLongOpt("threads")
                .withDescription("The number of threads to use.\n"
                        + "Default = 4 (influences processing speed)")
                .hasArg()
                .create("a"));
        options.addOption(OptionBuilder.withLongOpt("max_proteins")
                .withDescription("The number of protein matches a peptide can have and still be processed.\n"
                        + "Default = 100 (influences the processing speed)")
                .hasArg()
                .create("b"));
        options.addOption(OptionBuilder.withLongOpt("result_dir")
                .withDescription("The path to the directory to place the result file in.\n"
                        + "Default is ./")
                .hasArg()
                .create("c"));
        options.addOption(OptionBuilder.withLongOpt("digestion")
                .withDescription("The type of peptide digestion that is wanted:\n"
                        + "0:no digestion\n"
                        + "1:Trypsin conservative (only digest at Arg unless Pro after)\n"
                        + "\t\t\t\t\t2:Trypsin\n"
                        + "3:Pepsin High PH (PH > 2)\n"
                        + "4:Pepsin (PH 1.3)\n"
                        + "5:Chemotrypsin high specificity\n"
                        + "6:Chemotrypsin Low specificity\n"
                        + "Default = 0 (no digestion)")
                .hasArg()
                .create("d"));
        options.addOption(OptionBuilder.withLongOpt("min_peptide_length")
                .withDescription("Minimal length of the peptide to be used.\n"
                        + "Default = 6")
                .hasArg()
                .create("e"));
        options.addOption(OptionBuilder.withLongOpt("database")
                .withDescription("The path and name of a fasta file that contains the protein database.\n")
                .hasArg()
                .create("f"));
        options.addOption(OptionBuilder.withLongOpt("help")
                .withDescription("Prints the help if used")
                .create("h"));

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        // Check help option
        if (cmd.hasOption("h") || cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Help", options);
            System.exit(0);
        }
        // Checks the threads option
        if (cmd.hasOption("a") || cmd.hasOption("threads")) {
            if (checkValidIntegerOption(cmd.getOptionValue("a"), options, "The number of threads you entered is not a valid number.")) {
                this.arguments.put("a", cmd.getOptionValue("a"));
            }
        } else {
            this.arguments.put("a", "4");
        }
        // Checks the max proteins option
        if (cmd.hasOption("b") || cmd.hasOption("max_proteins")) {
            if (checkValidIntegerOption(cmd.getOptionValue("b"), options, "The number of proteins that a peptide can match to is not a valid number.")) {
                this.arguments.put("b", cmd.getOptionValue("b"));
            }
        } else {
            this.arguments.put("b", "100");
        }
        // Checks the result dir option
        if (cmd.hasOption("c") || cmd.hasOption("result_dir")) {
            if (checkValidDirectoryOption(cmd.getOptionValue("c"), options, "The directory for the final results is not a valid directory.")) {
                this.arguments.put("c", cmd.getOptionValue("c"));
            }
        } else {
            this.arguments.put("c", "./");
        }
        // checks the digestion option
        if (cmd.hasOption("d") || cmd.hasOption("digestion")) {
            if (checkValidIntegerOption(cmd.getOptionValue("d"), options, "The digestion you chose is not correct.\nPlease try again with a valid digestion method.")) {
                this.arguments.put("d", cmd.getOptionValue("d"));
            }
        } else {
            this.arguments.put("d", "0");
        }
        // Checks the minimal peptide length option
        if (cmd.hasOption("e") || cmd.hasOption("min_peptide_length")) {
            if (checkValidIntegerOption(cmd.getOptionValue("e"), options, "You should enter a valid number for the minimal length of a peptide.")) {
                this.arguments.put("e", cmd.getOptionValue("e"));
            }
        } else {
            this.arguments.put("e", "6");
        }
        // Checks the database option
        if (!cmd.hasOption("f") && !cmd.hasOption("database")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("You should provide a valid database to match the peptides against.\nThis file needs to be .fa or .fasta format", options);
            System.exit(0);
        } else {
            if (checkValidDatabaseFileOption(cmd.getOptionValue("f"), options, "You should provide a valid database to match the peptides against.\n"
                    + "This file has to be a valid .fa or .fasta format.")) {
                this.arguments.put("f", cmd.getOptionValue("f"));
            }
        }
        System.out.println(arguments);
        return arguments;
    }

    /**
     * Checks if the option value is a valid number.
     *
     * @param optionValue the value of the option
     * @param options the options object
     * @return true if option a/threads is a valid value
     */
    private boolean checkValidIntegerOption(final String optionValue, final Options options, final String errorMessage) {
        try {
            Integer check = Integer.parseInt(optionValue);
        } catch (IllegalArgumentException IAE) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(errorMessage, options);
            System.exit(0);
        }
        return true;
    }

    /**
     * Checks if the option value is a valid directory.
     *
     * @param optionValue the value of option
     * @param options the options object
     * @return true if option value is a valid directory
     */
    private boolean checkValidDirectoryOption(final String optionValue, final Options options, final String errorMessage) {
        File file = new File(optionValue);
        if (file.isDirectory()) {
            return true;
        } else {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(errorMessage, options);
            System.exit(0);
        }
        return false;
    }

    /**
     * Checks if the option value is a valid database file. Checks if the file
     * extension is .fa or .fasta and if it is a valid file. When that passes
     * checks if the first or second line startswith an ">" which is typical for
     * a fa and fasta file.
     *
     * @param optionValue the value of option
     * @param options the options object
     * @return true if option value is a valid database in .fa or .fasta format
     * @throws FileNotFoundException if the file is not found.
     * @throws IOException if an error occurs during in or output handling
     */
    private boolean checkValidDatabaseFileOption(final String optionValue, final Options options, final String errorMessage)
            throws FileNotFoundException, IOException {
        File file = new File(optionValue);
        if (file.isFile() && optionValue.endsWith(".fa") || optionValue.endsWith(".fasta")) {
            BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
            String line;
            Integer lineCount = 0;
            boolean validFasta = false;
            while ((line = br.readLine()) != null && lineCount < 2) {
                lineCount++;
                if (line.startsWith(">")) {
                    validFasta = true;
                }
            }
            if (validFasta) {
                return true;
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(errorMessage, options);
                System.exit(0);
            }
        } else {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(errorMessage, options);
            System.exit(0);
        }
        return false;
    }
}
