/**
 * Class:    GraphViewConstraints<br/>
 * <br/>
 * Created:  22.11.2013<br/>
 * Filename: GraphViewConstraints.java<br/>
 * Version:  $Revision: $<br/>
 * <br/>
 * last modified on $Date:  $<br/>
 *               by $Author: $<br/>
 * <br/>
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: $ -- $Revision: $ -- $Date: $
 * <br/>
 * (c) Sebastian A. Weiss, 2013 - All rights reserved.
 */

package de.wsdevel.components.plotter;

/**
 * GraphViewConstraints
 */
public class GraphViewConstraints {

    /** {@link Double} maxA */
    private Double maxA;

    /** {@link Double} maxB */
    private Double maxB;

    /** {@link Double} minA */
    private Double minA;

    /** {@link Double} minB */
    private Double minB;

    /**
     * @return the {@link Double} maxA
     */
    public Double getMaxA() {
	return this.maxA;
    }

    /**
     * @return the {@link Double} maxB
     */
    public Double getMaxB() {
	return this.maxB;
    }

    /**
     * @return the {@link Double} minA
     */
    public Double getMinA() {
	return this.minA;
    }

    /**
     * @return the {@link Double} minB
     */
    public Double getMinB() {
	return this.minB;
    }

    /**
     * @param maxA
     *            {@link Double} the maxA to set
     */
    public void setMaxA(final Double maxA) {
	this.maxA = maxA;
    }

    /**
     * @param maxB
     *            {@link Double} the maxB to set
     */
    public void setMaxB(final Double maxB) {
	this.maxB = maxB;
    }

    /**
     * @param minA
     *            {@link Double} the minA to set
     */
    public void setMinA(final Double minA) {
	this.minA = minA;
    }

    /**
     * @param minB
     *            {@link Double} the minB to set
     */
    public void setMinB(final Double minB) {
	this.minB = minB;
    }

}
// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: $
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================