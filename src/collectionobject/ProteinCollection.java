/*
 *Author: Arne Roeters
 */
package collectionobject;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author arne
 */
public class ProteinCollection {

    /**
     * Contains all the proteins. Key = name value = Protein Object
     */
    private final HashMap<String, Protein> proteins = new HashMap<>();

    /**
     * Returns the whole collection.
     *
     * @return HashMap name:Protein
     */
    public final HashMap<String, Protein> getProteins() {
        return proteins;
    }

    /**
     * adds a protein to the collection.
     *
     * @param proteinName String name of protein
     * @param protein Protein Object
     */
    public final void addProtein(final String proteinName,
            final Protein protein) {
        if (!proteins.containsKey(proteinName)) {
            this.proteins.put(proteinName, protein);
        } else {
            proteins.get(proteinName).addTotalPeptides(protein.getTotalPeptides());
        }
    }

    /**
     * Returns all protein names in the collection.
     *
     * @return Set of the protein Names
     */
    public final Set getProteinNames() {
        return (Set) proteins.keySet();
    }

    /**
     * Returns a single protein from the collection.
     *
     * @param proteinName name of the protein
     * @return Protein Object
     */
    public final Protein getProtein(final String proteinName) {
        return this.proteins.get(proteinName);
    }
}
