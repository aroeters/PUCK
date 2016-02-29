/*
 *Author: Arne Roeters
 */
package databaseconnection;

import java.util.HashMap;
import org.sql2o.Connection;
import java.util.List;
import org.sql2o.Sql2o;

/**
 *
 * @author arne
 */
public class ENSEMBLDatabaseConnector {

    /**
     * contains the sql2o.
     */
    private final Sql2o sql2o;
    /**
     * Contains the Uniprot to ENSG id conversion.
     */
    private final HashMap<String, String> uniprotToENSG = new HashMap<>();

    /**
     * Initiates the class.
     *
     * @throws java.lang.Exception when an error occurs
     */
    public ENSEMBLDatabaseConnector() throws Exception {
        this.sql2o = new Sql2o("jdbc:mysql://ensembldb.ensembl.org:5306/homo_sapiens_core_83_38", "anonymous", "");
        getUniprotToENSGDB();
    }

    /**
     * Returns a list with the query in it.
     *
     * @return List<Query>
     * @throws Exception when an error occurs
     */
    private List<Query> getQueries() throws Exception {
        System.out.println("Getting Uniprot ids from ensemble database");
        String sqlQuery = "SELECT g.stable_id as 'ENSG', c.dbprimary_acc as 'uniprotID' FROM gene g JOIN (SELECT t.gene_id, a.dbprimary_acc FROM transcript t JOIN "
                + "(SELECT t.transcript_id, b.dbprimary_acc FROM translation t JOIN "
                + "(SELECT DISTINCT x.dbprimary_acc, s.ensembl_id FROM object_xref s JOIN xref x ON x.xref_id = s.xref_id "
                + "WHERE x.external_db_id IN (2200, 2000) AND s.ensembl_object_type = 'Translation') b "
                + "ON t.translation_id = b.ensembl_id) a ON t.transcript_id = a.transcript_id) c ON g.gene_id=c.gene_id ORDER BY g.stable_id;";

        try (Connection con = sql2o.open()) {
            return (con.createQuery(sqlQuery)
                    .addColumnMapping("ENSG", "ENSG")
                    .addColumnMapping("uniprotID", "uniprotID")
                    .executeAndFetch(Query.class));
        }
    }

    /**
     * Fills the Uniprot to ENSG conversion HashMap.
     * @throws Exception when an error occurs
     */
    private void getUniprotToENSGDB() throws Exception {
        List<Query> entries = getQueries();
        for (Query item : entries) {
            this.uniprotToENSG.put(item.getUniprotID(), item.getENSG());
        }
        System.out.println("Finished collecting ids from ENSEMBL database");
    }
    /**
     * Returns the ENSG according to the given uniprot ID.
     * @param uniprot the uniprot id
     * @return ENSG
     */
    public final String getENSG(final String uniprot) {
        if (uniprotToENSG.containsKey(uniprot)) {
            return this.uniprotToENSG.get(uniprot);
        } else {
            return uniprot;
        }
    }
}
