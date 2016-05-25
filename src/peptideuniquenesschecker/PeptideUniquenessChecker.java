/*
 * Author: Arne Roeters
 */
package peptideuniquenesschecker;

import collectionobject.PeptideCollection;
import tools.CMDArgumentParser;
import collectionobject.ProteinCollection;
import fileparser.FastaDatabaseParser;
import fileparser.PeptideFileParser;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.cli.UnrecognizedOptionException;

/**
 *
 * @author arne
 */
public class PeptideUniquenessChecker {

    /**
     * The main that puts everything together.
     *
     * @param args command line arguments
     * @throws org.apache.commons.cli.ParseException when the parsing of the
     * arguments gives an exception.
     * @throws java.io.IOException when the I/O is wrong
     * @throws Exception when an error occurs
     */
    public static void main(final String[] args) throws IOException, Exception {
        CMDArgumentParser parser = new CMDArgumentParser();
        HashMap<String, String> cmdArguments = null;
        try {
        cmdArguments = parser.getCMDArguments(args);
        } catch (UnrecognizedOptionException e) {
            System.out.println("You used an unsupported argument. Please use -h to get the manual of the program");
        }
        FastaDatabaseParser databaseParser;
        if (cmdArguments.get("p") == null && cmdArguments.get("g") == null) {
            databaseParser = new FastaDatabaseParser(
                    cmdArguments.get("a").toString(),
                    Integer.parseInt(cmdArguments.get("b").toString()),
                    cmdArguments.get("c").toString(),
                    cmdArguments.get("f").toString(),
                    cmdArguments.get("d").toString(),
                    Integer.parseInt(cmdArguments.get("e").toString()),
                    cmdArguments.get("o").toString(),
                    Integer.parseInt(cmdArguments.get("m").toString()));
        } else if (cmdArguments.get("p") == null) {
            PeptideFileParser pfp = new PeptideFileParser();
            PeptideCollection externalPepCol = pfp.getPeptideCollectionByInput((String) cmdArguments.get("g").toString());
            databaseParser = new FastaDatabaseParser(
                    cmdArguments.get("a").toString(),
                    Integer.parseInt(cmdArguments.get("b").toString()),
                    cmdArguments.get("c").toString(),
                    cmdArguments.get("f").toString(),
                    cmdArguments.get("d").toString(),
                    Integer.parseInt(cmdArguments.get("e").toString()),
                    cmdArguments.get("o").toString(),
                    externalPepCol,
                    Integer.parseInt(cmdArguments.get("m").toString()));
        } else {
            PeptideFileParser pfp = new PeptideFileParser();
            PeptideCollection externalPepCol = pfp.getPeptideCollectionByFile(cmdArguments.get("p").toString());
            databaseParser = new FastaDatabaseParser(
                    cmdArguments.get("a").toString(),
                    Integer.parseInt(cmdArguments.get("b").toString()),
                    cmdArguments.get("c").toString(),
                    cmdArguments.get("f").toString(),
                    cmdArguments.get("d").toString(),
                    Integer.parseInt(cmdArguments.get("e").toString()),
                    cmdArguments.get("o").toString(),
                    externalPepCol,
                    Integer.parseInt(cmdArguments.get("m").toString()));
        }
        ProteinCollection proteinCollection = databaseParser.getProteinCollection();
        databaseParser.getPeptideUniqueness(proteinCollection, cmdArguments.get("z").toString());
    }

}
