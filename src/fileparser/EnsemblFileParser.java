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
    public final HashMap<String, String> getEnsemblID(final String filename) throws FileNotFoundException, IOException {
        System.out.println("Collecting ids from: " + filename);
        HashMap<String, String> idConversion = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(filename).getPath()));
        String line = br.readLine();
        String[] splitLine = line.split("\t");
        Integer ENSG;
        Integer otherID;
        if (splitLine[1].startsWith("ENSG")) ENSG = 1; else ENSG = 0;
        if (splitLine[1].startsWith("ENSG")) otherID = 0; else otherID = 1;
        while ((line=br.readLine()) != null) {
            splitLine = line.split("\t");
            idConversion.put(splitLine[otherID], splitLine[ENSG]);
        }
        return idConversion;
    }
}
