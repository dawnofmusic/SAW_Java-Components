package de.wsdevel.components.piemenue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.PopupFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.wsdevel.tools.awt.GUIToolkit;

/**
 * AppTestPieMenu created on 29.01.2009. for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 * @version $Author: sweiss $ -- $Revision: 1.2 $ -- $Date: 2009-02-02 16:23:41
 *          $
 * 
 * <br>
 *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
 *          Interactive Storytelling Framework
 */
public class AppTestPieMenu {

    /**
     * COMMENT.
     * 
     * @param args
     */
    @SuppressWarnings("serial")
    public static void main(String[] args) {
	PopupFactory.setSharedInstance(new PieMenuPopupFactory());

	final JFrame mainFrame = GUIToolkit.createMainFramOverPanel(
		new JPanel(), new Rectangle(new Dimension(800, 600)));

	final JPopupMenu popup = new JPopupMenu();
	popup.add(new JMenuItem(new AbstractAction("Hallo") {
	    public void actionPerformed(ActionEvent e) {
		System.out.println("Hallo");// TODO remove sysout
	    }
	}));
	final JMenuItem menuItem = new JMenuItem("Super");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		System.out.println("Super");// TODO remove sysout
	    }
	});
	popup.add(menuItem);
	final JMenuItem menuItem2 = new JMenuItem("Klasse");
	menuItem2.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		System.out.println("Klasse");// TODO remove sysout
	    }
	});
	popup.add(menuItem2);

	final JMenu menuItem3 = new JMenu("Untermenue");
	menuItem3.add(new JMenuItem("Alles klar"));
	popup.add(menuItem3);
	popup.add(new JMenuItem("Wahnsinn1"));
	popup.add(new JMenuItem("Wahnsinn2"));
	popup.add(new JMenuItem("Wahnsinn3"));
	popup.add(new JMenuItem("Wahnsinn4"));
	popup.add(new JMenuItem("Wahnsinn5"));

	mainFrame.getContentPane().addMouseListener(new MouseAdapter() {

	    @Override
	    public void mousePressed(MouseEvent e) {
		checkForPopup(mainFrame, popup, e);
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		checkForPopup(mainFrame, popup, e);
	    }

	    /**
	     * COMMENT.
	     * 
	     * @param mainFrameRef
	     * @param popupRef
	     * @param e
	     */
	    private void checkForPopup(final JFrame mainFrameRef,
		    final JPopupMenu popupRef, MouseEvent e) {
		if (e.isPopupTrigger()) {
		    popupRef.show(mainFrameRef.getContentPane(), e.getX(),
			    e.getY());
		}
	    }
	});

	new Timer().schedule(new TimerTask() {
	    @Override
	    public void run() {
		popup.show(mainFrame.getContentPane(), 100, 100);
	    }
	}, 200);

	mainFrame.setVisible(true);
    }

}
//
// $Log: AppTestPieMenu.java,v $
// Revision 1.2 2009-02-09 13:10:31 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:41 sweiss
// *** empty log message ***
//
//
