/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import collectionobject.Protein;
import collectionobject.ProteinCollection;
import collectionobject.PeptideCollection;
import tools.StringSearch;


/**
 *
 * @author arne
 */
public class CallablePeptideMatcher {

    /**
     * All peptides that are left after digestion.
     */
    private final PeptideCollection pepCol;
    /**
     * All proteins to match the peptides against.
     */
    private final ProteinCollection protCol;

    /**
     * Initiates the class.
     *
     * @param pepCol all peptides to search for
     * @param protCol all protein objects to search against
     * @throws Exception when an Error occurs
     */
    public CallablePeptideMatcher(final PeptideCollection pepCol,
            final ProteinCollection protCol) throws Exception {
        this.pepCol = pepCol;
        this.protCol = protCol;
    }

    /**
     * Subclass that can be called and used in a thread pool.
     */
    public static class CallableMatcher implements Callable {

        /**
         * The peptide that is searched for in the proteins, does not contain
         * the amino acid before and after.
         */
        private final String peptide;
        /**
         * The total protein database to search the peptide in.
         */
        private final ProteinCollection protCol;

        /**
         * Initiates the subclass.
         *
         * @param peptideEntry the peptide to search for in the proteins.
         * @param protCol the whole protein collection
         */
        public CallableMatcher(final String peptideEntry, final ProteinCollection protCol) {
            this.peptide = peptideEntry;
            this.protCol = protCol;
        }

        @Override
        public final LinkedList<String> call() {
            LinkedList<String> peptideMatches = new LinkedList<>();
            peptideMatches.add(peptide);
            StringSearch ss = new StringSearch(peptide);
            for (Protein protein : this.protCol.getProteins().values()) {
                if (ss.search(protein.getSequence())) {
                    peptideMatches.add(protein.getName());
                }
            }
            return peptideMatches;
        }
    }

    /**
     * Responsible for the multi threading and parsing.
     *
     * @param threadNumber Threads to use
     * @return the protein Collection
     * @throws Exception when an Error occurs
     */
    public final Set<Future<LinkedList<String>>> matchPeptides(final Integer threadNumber) throws Exception {
        // Creates a thread pool with the max number of threads that can be used.
        ExecutorService pool = Executors.newScheduledThreadPool(threadNumber);
        // creates a set with future objects which can be accessed after the all processes are completed
        Set<Future<LinkedList<String>>> set = new HashSet<>();
        Callable<LinkedList<String>> callable;
        
        for (String peptide : this.pepCol.getAllPeptides()) {
            callable = new CallableMatcher(peptide, this.protCol);
            Future<LinkedList<String>> future = pool.submit(callable);
            set.add(future);
        }
        // shuts down the threads or else the will keep running
        pool.shutdown();

        return set;
    }
}
