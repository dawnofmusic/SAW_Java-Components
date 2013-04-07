package de.wsdevel.components.table;

import javax.swing.ListModel;
import javax.swing.table.TableModel;

/**
 * Created on 18.12.2003.
 * 
 * for project: tools
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.2 $ -- $Date: 2005/10/26
 *          16:56:23 $
 */
public interface TableModelOverListModel extends TableModel {

    /**
     * COMMENT.
     * 
     * @return {@link ListModel}
     */
    ListModel getListModel();
}
/*
 * $Log: TableModelOverListModel.java,v $ Revision 1.2 2006-11-02 11:25:49
 * sweissTFH move to JDK 5
 * 
 * Revision 1.1 2006/05/02 16:06:00 sweissTFH cleaned up tools and moved
 * everything to appropriate new packages
 * 
 * Revision 1.4 2006/04/05 18:19:34 sweissTFH cleaned up checkstyle errors
 * 
 * Revision 1.3 2005/12/27 16:06:01 sweissTFH moved to java 5 and very big clean
 * up!
 * 
 * Revision 1.2 2005/10/26 16:56:23 mschneiderTFH start of very big clean up and
 * commenting! (sw)
 */
