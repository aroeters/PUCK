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
public class GeneCollection {
    /**
     * Contains all the proteins.
     * Key = name
     * value = Protein Object
     */
    private final HashMap<String, Gene> genes = new HashMap<>();
    /**
     * Returns the whole collection.
     * @return HashMap name:Gene
     */
    public final HashMap<String, Gene> getGenes() {
        return genes;
    }
    /**
     * adds a protein to the collection.
     * @param geneName String name of gene
     * @param gene Gene Object
     */
    public final void addGene(final String geneName,
            final Gene gene) {
        this.genes.put(geneName, gene);
    }
    /**
     * Returns all Gene names in the collection.
     * @return Set of the gene Names
     */
    public final Set getGeneNames() {
        return (Set) genes.keySet();
    }
        /**
     * Returns a single gene from the collection.
     * @param geneName name of the protein
     * @return Gene Object
     */
    public final Gene getGene(final String geneName) {
        return this.genes.get(geneName);
    }
    /**
     * Checks if the gene is present in the collection.
     * @param geneName name of the gene
     * @return  true if gene in the collection
     */
    public final Boolean checkGene(final String geneName) {
        return genes.containsKey(geneName);
    }
}
