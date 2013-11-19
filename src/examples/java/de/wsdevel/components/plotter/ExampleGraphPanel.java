/**
 * 
 */
package de.wsdevel.components.plotter;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import org.apache.log4j.BasicConfigurator;

import de.wsdevel.tools.math.Graph;
import de.wsdevel.tools.math.ValueTuple;

/**
 * @author Sebastian A. Weiss
 * 
 */
public final class ExampleGraphPanel {

    /**
     * @param args
     */
    public static void main(String[] args) {
	BasicConfigurator.configure();

	final GraphForComponent graphRef1 = createDummyGraph(Color.red,
		new Color(0, 255, 0, 128), 1024 * 1024);

	final GraphForComponent graphRef2 = createDummyGraph(Color.BLUE,
		new Color(255, 0, 255, 128), 3 * 7 * 41);

	final JFrame frame = new JFrame("Example GraphPanel"); //$NON-NLS-1$
	frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	final GraphPanel graphPanel = new GraphPanel();
	graphPanel.addGraph(graphRef1);
	graphPanel.addGraph(graphRef2);

	graphPanel.setBackgroundTitle("This is an example background title"); //$NON-NLS-1$

	final JScrollPane scrollPane = new JScrollPane(graphPanel);
	scrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	scrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	frame.getContentPane().add(scrollPane);
	frame.setSize(640, 480);
	frame.setVisible(true);

    }

    /**
     * @param color
     * @param fillColor
     * @return
     */
    private static GraphForComponent createDummyGraph(final Color color,
	    final Color fillColor, final long seed) {
	final Graph modelRef = new Graph();
	modelRef.setMaxNumberOfValues(15);
	modelRef.addTuple(new ValueTuple(0, 50));
	final GraphForComponent graphRef1 = new GraphForComponent(modelRef);
	graphRef1.setColor(color);
	graphRef1.setFillColor(fillColor);

	final long startMillis = System.currentTimeMillis();
	final Random random = new Random(seed);

	final Timer timer = new Timer();
	timer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		graphRef1.addTuple(new ValueTuple(
			(System.currentTimeMillis() - startMillis) / 1000d,
			random.nextDouble() * 100));
	    }
	}, 0, 1000);
	return graphRef1;
    }
}
