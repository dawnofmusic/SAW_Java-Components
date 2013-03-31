package de.wsdevel.components.plotter;

import java.awt.Color;
import java.awt.Stroke;

/**
 * Created on 26.05.2006.
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.1 $ -- $Date: 2006-05-26
 *          18:25:05 $
 */
public interface FunctionToPlot {

	/**
	 * COMMENT.
	 * 
	 * @param x
	 *            <code>double</code>
	 * @return <code>double</code>>
	 */
	double f(double x);

	/**
	 * COMMENT.
	 * 
	 * @return {@link Color }
	 */
	Color getColor();

	/**
	 * COMMENT.
	 * 
	 * @return <code>float</code>
	 */
	Stroke getStroke();

}
/*
 * $Log: FunctionToPlot.java,v $ Revision 1.1 2006-05-26 18:25:05 sweissTFH
 * added plotter panel and fermi function
 */
