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
     * Contains the otherID collected from the database.
     */
    private String otherID;
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
     * Returns the otherID that is linked to the ENSG.
     * @return String otherID
     */
    public final String getOtherID() {
        return otherID;
    }
    /**
     * Sets the otherID linked to the ENSG.
     * @param UniprotID String otherID
     */
    public final void setOtherID(final String UniprotID) {
        this.otherID = UniprotID;
    }

    @Override
    public String toString() {
        return "Query{" + "ENSG=" + ENSG + ", otherID=" + otherID + '}';
    }
    
}
