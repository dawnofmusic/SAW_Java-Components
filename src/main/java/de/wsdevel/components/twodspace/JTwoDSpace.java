package de.wsdevel.components.twodspace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;

import de.wsdevel.components.WSRoot;

/**
 * Created on 28.04.2009.
 * 
 * for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:26:34 $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class JTwoDSpace extends WSRoot {

    /**
     * <code>long</code> serial version unique id.
     */
    private static final long serialVersionUID = 1168067974790982142L;

    /**
     * {@link JCross} COMMENT.
     */
    private JCross innerCross;

    /**
     * COMMENT.
     */
    public JTwoDSpace() {
	this(new Cross());
    }

    /**
     * COMMENT.
     * 
     * @param cross
     *            {@link Cross}
     */
    public JTwoDSpace(final Cross cross) {
	setBackground(Color.WHITE);
	setForeground(Color.BLACK);
	setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

	this.innerCross = new JCross(cross);
	add(this.innerCross);
	// getModel().addPropertyChangeListener(new PropertyChangeListener() {
	// public void propertyChange(PropertyChangeEvent evt) {
	// Cross model = getModel();
	//
	// float greenPart = 0;
	// float redPart = 0;
	// float bluePart = 0;
	// redPart = 1 - (float) (model.getX() + model.getY()) / 2;
	// if (redPart > 1) {
	// redPart = 1;
	// }
	// greenPart = 1 - ((float) (model.getX() + model.getY()) / -2);
	// if (greenPart > 1) {
	// greenPart = 1;
	// }
	// bluePart = 1 - (float) Math.abs((model.getX()));
	//
	// setBackground(new Color(redPart, greenPart, bluePart));
	// }
	// });
	addComponentListener(new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentResized(ComponentEvent e) {
		JTwoDSpace.this.innerCross.updateLocation(getModel(),
			JTwoDSpace.this);
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param d
     *            {@link Dimension}
     * @return {@link Dimension}
     */
    private Dimension createSquareDimension(final Dimension d) {
	int max = d.width;
	if (d.height > d.width) {
	    max = d.height;
	}
	Dimension dimension = new Dimension(max, max);
	return dimension;
    }

    /**
     * @return {@link Cross}
     * @see de.wsdevel.components.WSComponent#getModel()
     */
    public final Cross getModel() {
	return this.innerCross.getModel();
    }

    /**
     * @return {@link boolean} the editable.
     */
    public final boolean isEditable() {
	return this.innerCross.isMovable();
    }

    /**
     * @param g
     *            {@link Graphics}
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected final void paintComponent(final Graphics g) {
	g.setColor(getBackground());
	g.fillRect(0, 0, getWidth(), getHeight());

	g.setColor(getForeground());
	int halfWidth = (getWidth() / 2) - 1;
	g.drawLine(halfWidth, 0, halfWidth, getHeight());
	int halfHeight = (getHeight() / 2) - 1;
	g.drawLine(0, halfHeight, getWidth(), halfHeight);

	g.setColor(Color.LIGHT_GRAY);
	g.setFont(new Font("sansserif", Font.PLAIN, 30));
	Rectangle2D stringBounds = g.getFontMetrics().getStringBounds("+/+", g);
	g.drawString("+/+", halfWidth
		+ (int) Math.round((halfWidth - stringBounds.getWidth()) / 2),
		(int) Math.round(stringBounds.getHeight()));
	stringBounds = g.getFontMetrics().getStringBounds("-/+", g);
	g.drawString("-/+", (int) Math.round((halfWidth - stringBounds
		.getWidth()) / 2), (int) Math.round(stringBounds.getHeight()));

	stringBounds = g.getFontMetrics().getStringBounds("+/-", g);
	g.drawString("+/-", halfWidth
		+ (int) Math.round((halfWidth - stringBounds.getWidth()) / 2),
		halfHeight + (int) Math.round(stringBounds.getHeight()));

	stringBounds = g.getFontMetrics().getStringBounds("-/-", g);
	g.drawString("-/-", (int) Math.round((halfWidth - stringBounds
		.getWidth()) / 2), halfHeight
		+ (int) Math.round(stringBounds.getHeight()));

    }

    /**
     * @param editableVal
     *            {@link boolean} the editable to set.
     */
    public final void setEditable(final boolean editableVal) {
	this.innerCross.setMovable(false);
    }

    /**
     * @param modelRef
     *            {@link Cross}
     * @see de.wsdevel.components.WSComponent#setModel(java.lang.Object)
     */
    public final void setModel(final Cross modelRef) {
	this.innerCross.setModel(modelRef);
    }

    /**
     * @param d
     *            {@link Dimension}
     * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
     */
    @Override
    public final void setPreferredSize(final Dimension d) {
	final Dimension dim = createSquareDimension(d);
	super.setPreferredSize(dim);
	setMinimumSize(dim);
    }

}
//
// $Log: JTwoDSpace.java,v $
// Revision 1.1  2009-05-02 11:26:34  sweiss
// added JTwoDSpace
//
//
