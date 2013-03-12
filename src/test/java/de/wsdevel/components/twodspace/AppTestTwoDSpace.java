package de.wsdevel.components.twodspace;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.BasicConfigurator;

import de.wsdevel.tools.awt.GBC;
import de.wsdevel.tools.awt.GUIToolkit;

/**
 * Created on 29.04.2009 for project Java__Components. <br/>
 * (c) 2009, wei&szlig;&amp;schmidt - All rights reserved.
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Wei&szlig; -
 *         wei&szlig;&amp;schmidt</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:27:05 $
 */
public class AppTestTwoDSpace {

    /**
     * @param args
     *            {@link String}[]
     */
    public static void main(final String[] args) {
	BasicConfigurator.configure();

	JPanel panel = new JPanel(new GridBagLayout());
	JFrame frame = GUIToolkit.createMainFramOverPanel(panel, new Rectangle(
		new Dimension(400, 400)));
	final JTwoDSpace space = new JTwoDSpace();
	space.setPreferredSize(new Dimension(300, 300));
	panel.add(space, new GBC().pos(0, 0).anchorCenter());
	GUIToolkit.center(frame);
	frame.addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(ComponentEvent e) {
		space.setModel(new Cross(0, 0));
	    }
	});
	frame.setVisible(true);
    }

}
//
// $Log: AppTestTwoDSpace.java,v $
// Revision 1.1  2009-05-02 11:27:05  sweiss
// added JTwoDSpace
//
//
