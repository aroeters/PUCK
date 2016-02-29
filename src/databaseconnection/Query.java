/*
 *Author: Arne Roeters
 */
package databaseconnection;

/**
 *
 * @author arne
 */
public class Query {
    /**
     * Contains the ENSG collected from the database.
     */
    private String ENSG;
    /**
     * Contains the uniprotID collected from the database.
     */
    private String uniprotID;
    /**
     * Returns the ENSG stable id.
     * @return String ENSG
     */
    public final String getENSG() {
        return ENSG;
    }
    /**
     * Sets the ENSG.
     * @param ENSG String ENSG
     */
    public final void setENSG(final String ENSG) {
        this.ENSG = ENSG;
    }
    /**
     * Returns the uniprotID that is linked to the ENSG.
     * @return String uniprotID
     */
    public final String getUniprotID() {
        return uniprotID;
    }
    /**
     * Sets the uniprotID linked to the ENSG.
     * @param UniprotID String uniprotID
     */
    public final void setUniprotID(final String UniprotID) {
        this.uniprotID = UniprotID;
    }

    @Override
    public String toString() {
        return "Query{" + "ENSG=" + ENSG + ", uniprotID=" + uniprotID + '}';
    }
    
}
