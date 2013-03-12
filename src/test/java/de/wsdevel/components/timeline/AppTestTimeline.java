package de.wsdevel.components.timeline;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.wsdevel.components.timeline.Timeline.ValueCat;
import de.wsdevel.tools.awt.GBC;
import de.wsdevel.tools.awt.GUIToolkit;

/**
 * Created on 22.11.2008.
 * 
 * for project: Scenejo__Model
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:27:05
 *          $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class AppTestTimeline {

    /**
     * @param args
     *            {@link String}[]
     */
    public static void main(final String[] args) {

	final JPanel panel = new JPanel(new GridBagLayout());
	final Timeline<Double> timeline = new Timeline<Double>(
		new Timeline.ValueAdapter<Double>() {

		    public double getDoubleForValue(Double value) {
			return value;
		    }

		    public Double getValueForDouble(double d) {
			return d;
		    }

		    public List<ValueCat<Double>> getCategories() {
			LinkedList<ValueCat<Double>> cats = new LinkedList<ValueCat<Double>>();
			cats.add(new Timeline.ValueCat<Double>("1.0", 1.0));
			cats.add(new Timeline.ValueCat<Double>("0.5", 0.5));
			cats.add(new Timeline.ValueCat<Double>("0", 0d));
			return cats;
		    }

		    public Double getStartValue() {
			return 0d;
		    }
		});
	final JTimeline<Double> timelineComp = new JTimeline<Double>(timeline);
	panel.add(timelineComp, new GBC().pos(0, 0).fillBoth(1, 1).insets(5, 5,
		5, 5));
	final JSlider slider = new JSlider(SwingConstants.HORIZONTAL,
		timelineComp.getMinimumMillisPerPixel(), timelineComp
			.getMaximumMillisPerPixel(), timelineComp
			.getMillisPerPixel());
	slider.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		timelineComp.setMillisPerPixel(slider.getValue());
	    }
	});
	panel.add(slider, new GBC().pos(0, 1).insets(0, 5, 5, 5)
		.fillHorizontal(1.0));

	final JFrame frame = GUIToolkit.createMainFramOverPanel(panel,
		new Rectangle(new Dimension(400, 200)));
	GUIToolkit.center(frame);
	frame.setVisible(true);
    }

}
//
// $Log: AppTestTimeline.java,v $
// Revision 1.1 2009-05-02 11:27:05 sweiss
// added JTwoDSpace
//
// Revision 1.1 2009-02-02 16:23:41 sweiss
// *** empty log message ***
//
//
