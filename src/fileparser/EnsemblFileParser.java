/**
 *
 * @author arne
 */
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author arne
 */
public class EnsemblFileParser {
    /**
     * Getter of the ensembl id file parser.
     * @param filename name of the file that contains the ids
     * @return HashMap with the converion in it.
     * @throws FileNotFoundException when the file is not found
     * @throws IOException 
     */
    public final HashMap<String, String> getEnsemblID(final String filename) throws FileNotFoundException, IOException {
        System.out.println("Collecting ids from: " + filename);
        HashMap<String, String> idConversion = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(filename).getPath()));
        String line = br.readLine();
        String[] splitLine = line.split("\t");
        Integer ensg;
        Integer otherId;
        if (splitLine[1].startsWith("ENSG")) ensg = 1; else ensg = 0;
        if (splitLine[1].startsWith("ENSG")) otherId = 0; else otherId = 1;
        while ((line = br.readLine()) != null) {
            splitLine = line.split("\t");
            idConversion.put(splitLine[otherId], splitLine[ensg]);
        }
        return idConversion;
    }
}
