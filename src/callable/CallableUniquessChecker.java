/*
 *Author: Arne Roeters
 */
package callable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import collectionobject.Protein;
import collectionobject.ProteinCollection;
import collectionobject.Gene;
import collectionobject.GeneCollection;
import collectionobject.PeptideCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author arne
 */
public class CallableUniquessChecker {

    /**
     * All peptides that are left after digestion.
     */
    private final PeptideCollection pepCol;
    /**
     * All proteins to match the peptides against.
     */
    private final Collection object;
    /**
     * True is it is a gene collection
     */
    private final Boolean geneCollection;

    /**
     * Initiates the class with a gene collection.
     *
     * @param pepCol all peptides that are found
     * @param geneCol all gene Objects in a collection
     * @throws Exception when an Error occurs
     */
    public CallableUniquessChecker(final PeptideCollection pepCol,
            final GeneCollection geneCol) throws Exception {
        this.pepCol = pepCol;
        this.object = geneCol.getGenes().values();
        this.geneCollection = true;
    }

    /**
     * Initiates the class with a protein collection.
     *
     * @param pepCol all peptides that are found
     * @param protCol all protein Objects in a collection
     * @throws Exception when an Error occurs
     */
    public CallableUniquessChecker(final PeptideCollection pepCol,
            final ProteinCollection protCol) throws Exception {
        this.pepCol = pepCol;
        this.object = protCol.getProteins().values();
        this.geneCollection = false;
    }

    /**
     * Subclass that can be called and used in a thread pool.
     */
    public static class CallableChecker implements Callable {

        /**
         * The gene object.
         */
        private final Gene gene;
        /**
         * The protein object;
         */
        private final Protein protein;
        /**
         * The total peptide Collection
         */
        private final PeptideCollection pepCollection;
        /**
         * True if the object is a gene.
         */
        private final boolean isGene;

        /**
         * Initiates the subclass.
         *
         * @param pepCol the whole peptide collection.
         * @param gene a gene object
         */
        public CallableChecker(final Gene gene, final PeptideCollection pepCol) {
            this.isGene = true;
            this.gene = gene;
            this.pepCollection = pepCol;
            this.protein = null;
        }

        /**
         * Initiates the subclass.
         *
         * @param pepCol the whole peptide collection.
         * @param protein a protein object
         */
        public CallableChecker(final Protein protein, final PeptideCollection pepCol) {
            this.isGene = false;
            this.protein = protein;
            this.pepCollection = pepCol;
            this.gene = null;
        }

        @Override
        public final HashMap<String, ArrayList<String>> call() {
            String peptideSequence;
            HashMap<String, ArrayList<String>> info = new HashMap<>();
            if (this.isGene) {
                info.put(this.gene.getName(), null);
                ArrayList<String> unique = new ArrayList<>();
                for (Integer peptide : this.gene.getUniquePeptides()) {
                    if (gene.checkTotalPeptide(peptide)) {
                        unique.add(this.pepCollection.getPeptideSequence(peptide) + "+");
                    } else {
                        unique.add(this.pepCollection.getPeptideSequence(peptide));
                    }
                }
                info.put("unique", unique);
                ArrayList<String> non_unique = new ArrayList<>();
                for (Integer peptide : this.gene.getNonUniquePeptides()) {
                    if (gene.checkTotalPeptide(peptide)) {
                        non_unique.add(this.pepCollection.getPeptideSequence(peptide) + "+");
                    } else {
                        non_unique.add(this.pepCollection.getPeptideSequence(peptide));
                    }
                }
                info.put("non_unique", non_unique);
            } else {
                info.put(this.protein.getName(), null);
                ArrayList<String> unique = new ArrayList<>();
                for (Integer peptide : this.protein.getUniquePeptides()) {
                    if (protein.checkTotalPeptide(peptide)) {
                        unique.add(this.pepCollection.getPeptideSequence(peptide) + "+");
                    } else {
                        unique.add(this.pepCollection.getPeptideSequence(peptide));
                    }
                }
                info.put("unique", unique);
                ArrayList<String> non_unique = new ArrayList<>();
                for (Integer peptide : this.protein.getNonUniquePeptides()) {
                    if (protein.checkTotalPeptide(peptide)) {
                        non_unique.add(this.pepCollection.getPeptideSequence(peptide) + "+");
                    } else {
                        non_unique.add(this.pepCollection.getPeptideSequence(peptide));
                    }
                }
                info.put("non_unique", non_unique);
            }
            return info;
        }
    }

    /**
     * Responsible for the multi threading and parsing.
     *
     * @param threadNumber Threads to use
     * @return the protein Collection
     * @throws Exception when an Error occurs
     */
    public final Set<Future<HashMap<String, ArrayList<String>>>> matchPeptides(final Integer threadNumber) throws Exception {
        // Creates a thread pool with the max number of threads that can be used.
        ExecutorService pool = Executors.newScheduledThreadPool(threadNumber);
        // creates a set with future objects which can be accessed after the all processes are completed
        Set<Future<HashMap<String, ArrayList<String>>>> set = new HashSet<>();
        Callable<HashMap<String, ArrayList<String>>> callable;
        for (Object instance : this.object) {
            if (this.geneCollection) {
                callable = new CallableChecker((Gene) instance, pepCol);
            } else {
                callable = new CallableChecker((Protein) instance, pepCol);
            }
            Future<HashMap<String, ArrayList<String>>> future = pool.submit(callable);
            set.add(future);
        }
        // shuts down the threads or else the will keep running
        pool.shutdown();
        return set;
    }
}
