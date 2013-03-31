package de.wsdevel.components.header;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.wsdevel.tools.awt.GBC;

/**
 * Created on 10. Mai 2003, 01:18
 * 
 * for project: tools
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweiss $ -- $Revision: 1.6 $ -- $Date: 2005/10/31 19:11:33
 *          $
 */
public class DefaultHeaderPanel extends JPanel {

	/**
	 * <code>long</code> serial version unique id.
	 */
	private static final long serialVersionUID = 8551294412812067368L;

	/**
	 * {@link Color} COMMENT.
	 */
	private static final Color BACKGROUND = Color.gray;

	/**
	 * {@link Color} COMMENT.
	 */
	private static final Color TITLE_COLOR = new java.awt.Color(255, 255, 255);

	/**
	 * {@link Font} COMMENT.
	 */
	private static final Font FONT_22 = new Font("Dialog", 0, 22);

	/**
	 * {@link Font} COMMENT.
	 */
	private static final Font FONT_16 = new Font("Dialog", 0, 14);

	/**
	 * {@link JLabel} COMMENT.
	 */
	private JLabel iconLabel;

	/**
	 * {@link JLabel} COMMENT.
	 */
	private JLabel captionLabel;

	/**
	 * {@link ImageIcon} COMMENT.
	 */
	private ImageIcon background;

	/**
	 * COMMENT.
	 * 
	 * @param caption
	 *            {@link String}
	 * @param backgroundVal
	 *            {@link ImageIcon}
	 */
	public DefaultHeaderPanel(final String caption,
			final ImageIcon backgroundVal) {
		this(null, caption, backgroundVal);
	}

	/**
	 * COMMENT.
	 * 
	 * @param icon
	 *            {@link Icon}
	 * @param caption
	 *            {@link String}
	 * @param backgroundVal
	 *            {@link ImageIcon}
	 */
	public DefaultHeaderPanel(final Icon icon, final String caption,
			final ImageIcon backgroundVal) {
		setOpaque(true);
		setLayout(new GridBagLayout());
		this.background = backgroundVal;
		add(createFiller(), new GBC().pos(2, 0).fillBoth(1.0, 1.0));
		setBackground(BACKGROUND);
		setOpaque(false);

		setIcon(icon);
		setCaption(caption);
	}

	/**
	 * COMMENT.
	 * 
	 * @param icon
	 *            {@link Icon}
	 */
	public void setIcon(final Icon icon) {
		if (this.iconLabel != null) {
			remove(this.iconLabel);
			this.iconLabel = null;
		}
		if (icon != null) {
			this.iconLabel = new JLabel();
			this.iconLabel.setOpaque(false);
			add(this.iconLabel,
					new GBC().pos(0, 0).anchorWest().insets(10, 10, 10, 0));
			this.iconLabel.setIcon(icon);
		}
	}

	/**
	 * @return {@link Dimension}
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public final Dimension getPreferredSize() {
		Dimension dimension = super.getPreferredSize();
		if (this.background != null
				&& this.background.getIconHeight() > dimension.height) {
			return new Dimension(dimension.width,
					this.background.getIconHeight());
		}
		return dimension;
	}

	/**
	 * COMMENT.
	 * 
	 * @param icon
	 *            {@link Icon}
	 * @param caption
	 *            {@link String}
	 */
	public DefaultHeaderPanel(final Icon icon, final String caption) {
		this(icon, caption, null);
	}

	/**
	 * Has to be called after {@link DefaultHeaderPanel#setIcon(Icon)} to set
	 * font size correctly!.
	 * 
	 * @param caption
	 *            {@link String}
	 */
	public final void setCaption(final String caption) {
		if (this.captionLabel != null) {
			remove(this.captionLabel);
			this.captionLabel = null;
		}
		if (caption != null) {
			this.captionLabel = createTitleLable();
			this.captionLabel.setOpaque(false);
			this.captionLabel.setBackground(BACKGROUND);
			add(this.captionLabel,
					new GBC().pos(1, 0).anchorWest().insets(10, 10, 10, 10));

			this.captionLabel.setText(caption);
		}
	}

	/**
	 * COMMENT.
	 * 
	 * @param color
	 *            {@link Color}
	 */
	public final void setCaptionColor(final Color color) {
		this.captionLabel.setForeground(color);
	}

	/**
	 * COMMENT.
	 * 
	 * @return {@link JPanel}
	 */
	private JPanel createFiller() {
		JPanel filler = new JPanel();
		filler.setOpaque(false);
		return filler;
	}

	/**
	 * COMMENT.
	 * 
	 * @return {@link JLabel}
	 */
	private JLabel createTitleLable() {
		JLabel title = new JLabel();
		title.setFont(FONT_22);
		if (this.iconLabel != null) {
			final int iconHeight = this.iconLabel.getIcon().getIconHeight();
			if (iconHeight > 16) {
				title.setFont(FONT_22);
			} else {
				title.setFont(FONT_16);
			}
		}
		title.setForeground(TITLE_COLOR);
		return title;
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
		if (this.background != null) {
			int yPos = this.background.getIconHeight() < getHeight() ? (getHeight() - this.background
					.getIconHeight()) / 2 : 0;
			this.background.paintIcon(this, g, getBounds().width
					- this.background.getIconWidth(), yPos);
		}
		super.paintComponent(g);
	}

}
//
// $Log: DefaultHeaderPanel.java,v $
// Revision 1.6 2009-06-14 15:06:19 sweiss
// fixes in DefaultHeaderPanel
//
// Revision 1.5 2009-05-02 11:24:16 sweiss
// size of header panel depends on icon size
//
// Revision 1.4 2007-08-03 21:41:21 sweiss
// image loading
//
// Revision 1.3 2006/11/04 10:54:58 sweissTFH
// smaller changes to default header
// panel
//
// Revision 1.2 2006/06/10 13:00:33 sweissTFH
// cleanup and smaller changes due to
// new compiler settings
//
// Revision 1.1 2006/05/02 16:06:00 sweissTFH
// cleaned up tools and moved
// everything to appropriate new packages
//
// Revision 1.4 2006/04/05 18:19:34 sweissTFH
// cleaned up checkstyle errors
//
// Revision 1.3 2005/12/27 16:06:01 sweissTFH
// moved to java 5 and very big clean
// up!
//
// Revision 1.2 2005/10/31 19:11:33 sweissTFH
// cleaned up and commented a bit
//
