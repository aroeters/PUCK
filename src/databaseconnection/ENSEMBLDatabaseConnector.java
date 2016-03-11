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
    private final HashMap<String, String> otherIDToENSG = new HashMap<>();

    /**
     * Initiates the class.
     *
     * @throws java.lang.Exception when an error occurs
     */
    public ENSEMBLDatabaseConnector() throws Exception {
        this.sql2o = new Sql2o("jdbc:mysql://ensembldb.ensembl.org:5306/homo_sapiens_core_83_38", "anonymous", "");
        getIDToENSGDB();
    }

    /**
     * Returns a list with the query in it.
     *
     * @return List<Query>
     * @throws Exception when an error occurs
     */
    private List<Query> getUniprotQueries() throws Exception {
        System.out.println("Getting Uniprot ids from ensemble database");
        String sqlQuery = "SELECT g.stable_id as 'ENSG', c.dbprimary_acc as 'uniprotID' FROM gene g JOIN (SELECT t.gene_id, a.dbprimary_acc FROM transcript t JOIN "
                + "(SELECT t.transcript_id, b.dbprimary_acc FROM translation t JOIN "
                + "(SELECT DISTINCT x.dbprimary_acc, s.ensembl_id FROM object_xref s JOIN xref x ON x.xref_id = s.xref_id "
                + "WHERE x.external_db_id IN (2200, 2000) AND s.ensembl_object_type = 'Translation') b "
                + "ON t.translation_id = b.ensembl_id) a ON t.transcript_id = a.transcript_id) c ON g.gene_id=c.gene_id ORDER BY g.stable_id;";

        try (Connection con = sql2o.open()) {
            List<Query> query_result = (con.createQuery(sqlQuery)
                    .addColumnMapping("ENSG", "ENSG")
                    .addColumnMapping("uniprotID", "otherID")
                    .executeAndFetch(Query.class));
            con.close();
            return query_result;
        } catch (Exception e) {
            System.out.println("The ensembl databases are not responding (correctly).\n"
                    + "Please try again a little later.\n"
                    + "If this problem keeps occuring contact the developer.");
            System.exit(0);
        }
        return null;
    }

    /**
     * Returns a list with the query in it.
     *
     * @return List<Query>
     * @throws Exception when an error occurs
     */
    private List<Query> getENSTQueries() throws Exception {
        System.out.println("Getting ENST from ensemble database");
        String sqlQuery = "SELECT g.stable_id as 'ENSG', t.stable_id as 'ENST' FROM transcript t, gene g where t.gene_id = g.gene_id;";

        try (Connection con = sql2o.open()) {
            List<Query> query_result = (con.createQuery(sqlQuery)
                    .addColumnMapping("ENSG", "ENSG")
                    .addColumnMapping("ENST", "otherID")
                    .executeAndFetch(Query.class));
            con.close();
            return query_result;
        } catch (Exception e) {
            System.out.println("The ensembl databases are not responding (correctly).\n"
                    + "Please try again a little later.\n"
                    + "If this problem keeps occuring contact the developer.");
            System.exit(0);
        }
        return null;
    }

    /**
     * Fills the ID to ENSG conversion HashMap.
     *
     * @throws Exception when an error occurs
     */
    private void getIDToENSGDB() throws Exception {
        List<Query> entries = getUniprotQueries();
        for (Query item : entries) {
            this.otherIDToENSG.put(item.getOtherID(), item.getENSG());
        }
        List<Query> ENST = getENSTQueries();
        for (Query item : ENST) {
            this.otherIDToENSG.put(item.getOtherID(), item.getENSG());
        }
        System.out.println("Done...");
    }

    /**
     * Returns the ENSG according to the given lookupID.
     *
     * @param lookupID the lookupID
     * @return ENSG
     */
    public final String getENSG(final String lookupID) {
        if (otherIDToENSG.containsKey(lookupID)) {
            return this.otherIDToENSG.get(lookupID);
        } else {
            return lookupID;
        }
    }
    /**
     * Returns the HashMap with all conversions in it
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getIDConversion() {
        return this.otherIDToENSG;
    }
}
