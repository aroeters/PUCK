/*
 * Author: Arne Roeters
 */
package proteinquantifier;

import Tools.CMDArgumentParser;
import collectionobject.ProteinCollection;
import fileparser.FastaDatabaseParser;
import java.io.IOException;
import java.util.HashMap;

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
    public static void main(final String[] args) throws IOException, Exception {
        CMDArgumentParser parser = new CMDArgumentParser();
        HashMap cmdArguments = parser.getCMDArguments(args);
        FastaDatabaseParser databaseParser = new FastaDatabaseParser(
                cmdArguments.get("a").toString(),
                Integer.parseInt(cmdArguments.get("b").toString()),
                cmdArguments.get("c").toString(),
                cmdArguments.get("f").toString(),
                cmdArguments.get("d").toString(),
                Integer.parseInt(cmdArguments.get("e").toString()));
        ProteinCollection proteinCollection = databaseParser.getProteinCollection();
        databaseParser.getPeptideUniqueness(proteinCollection);
        
    }

}
