package de.wsdevel.components.table;

import javax.swing.table.TableModel;

/**
 * Created on 26.10.2005.
 * 
 * for project: tools
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.1 $ -- $Date: 2005/10/26
 *          16:56:23 $ <br>
 */
public interface SortedTableModel extends TableModel {

	/**
	 * COMMENT.
	 * 
	 * @return <code>int</code>
	 */
	int getSortedColumn();

	/**
	 * COMMENT.
	 * 
	 * @return <code>boolean</code>
	 */
	boolean getAscending();
}
/*
 * $Log: SortedTableModel.java,v $ Revision 1.1 2006-05-02 16:06:00 sweissTFH
 * cleaned up tools and moved everything to appropriate new packages
 * 
 * Revision 1.3 2006/04/05 18:19:34 sweissTFH cleaned up checkstyle errors
 * Revision 1.2 2005/10/26 16:56:23 mschneiderTFH start of very big clean up and
 * commenting! (sw)
 */
