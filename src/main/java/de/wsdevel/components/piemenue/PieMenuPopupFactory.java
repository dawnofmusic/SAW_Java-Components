/*
 * @(#)PopupFactory.java	1.26 05/02/22
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package de.wsdevel.components.piemenue;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.MenuElement;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

/**
 * <code>PopupFactory</code>, as the name implies, is used to obtain instances
 * of <code>Popup</code>s. <code>Popup</code>s are used to display a
 * <code>Component</code> above all other <code>Component</code>s in a
 * particular containment hierarchy. The general contract is that once you have
 * obtained a <code>Popup</code> from a <code>PopupFactory</code>, you must
 * invoke <code>hide</code> on the <code>Popup</code>. The typical usage is:
 * 
 * <pre>
 *   PopupFactory factory = PopupFactory.getSharedInstance();
 *   Popup popup = factory.getPopup(owner, contents, x, y);
 *   popup.show();
 *   ...
 *   popup.hide();
 * </pre>
 * 
 * @see Popup
 * 
 * @version 1.26 02/22/05
 * @since 1.4
 */
public class PieMenuPopupFactory extends PopupFactory {

    /**
     * Max number of items to store in any one particular cache.
     */
    private static final int MAX_CACHE_SIZE = 5;

    /**
     * Key used to indicate a light weight popup should be used.
     */
    static final int LIGHT_WEIGHT_POPUP = 0;

    /**
     * Key used to indicate a medium weight Popup should be used.
     */
    static final int MEDIUM_WEIGHT_POPUP = 1;

    /*
     * Key used to indicate a heavy weight Popup should be used.
     */
    static final int HEAVY_WEIGHT_POPUP = 2;

    /**
     * Default type of Popup to create.
     */
    private int popupType = LIGHT_WEIGHT_POPUP;

    /**
     * Key used for client property to force heavy weight popups for a
     * component.
     */
    static final StringBuffer forceHeavyWeightPopupKey = new StringBuffer(
	    "__force_heavy_weight_popup__");

    /**
     * Provides a hint as to the type of <code>Popup</code> that should be
     * created.
     */
    void setPopupType(int type) {
	this.popupType = type;
    }

    /**
     * Returns the preferred type of Popup to create.
     */
    int getPopupType() {
	return this.popupType;
    }

    /**
     * Creates a <code>Popup</code> for the Component <code>owner</code>
     * containing the Component <code>contents</code>. <code>owner</code> is
     * used to determine which <code>Window</code> the new <code>Popup</code>
     * will parent the <code>Component</code> the <code>Popup</code> creates to.
     * A null <code>owner</code> implies there is no valid parent.
     * <code>x</code> and <code>y</code> specify the preferred initial location
     * to place the <code>Popup</code> at. Based on screen size, or other
     * paramaters, the <code>Popup</code> may not display at <code>x</code> and
     * <code>y</code>.
     * 
     * @param owner
     *            Component mouse coordinates are relative to, may be null
     * @param contents
     *            Contents of the Popup
     * @param x
     *            Initial x screen coordinate
     * @param y
     *            Initial y screen coordinate
     * @exception IllegalArgumentException
     *                if contents is null
     * @return Popup containing Contents
     */
    @Override
    public Popup getPopup(Component owner, Component contents, int x, int y)
	    throws IllegalArgumentException {
	if (contents == null) {
	    throw new IllegalArgumentException(
		    "Popup.getPopup must be passed non-null contents");
	}

	int popupType1 = getPopupType(owner, contents, x, y);
	Popup popup = getPopup(owner, contents, x, y, popupType1);

	if (popup == null) {
	    // Didn't fit, force to heavy.
	    popup = getPopup(owner, contents, x, y, HEAVY_WEIGHT_POPUP);
	}
	return popup;
    }

    /**
     * Returns the popup type to use for the specified parameters.
     */
    private int getPopupType(Component owner, Component contents, int ownerX,
	    int ownerY) {
	int popupType1 = getPopupType();

	if (owner == null || invokerInHeavyWeightPopup(owner)) {
	    popupType1 = HEAVY_WEIGHT_POPUP;
	} else if (popupType1 == LIGHT_WEIGHT_POPUP
		&& !(contents instanceof JToolTip)
		&& !(contents instanceof JPopupMenu)) {
	    popupType1 = MEDIUM_WEIGHT_POPUP;
	}

	// Check if the parent component is an option pane. If so we need to
	// force a heavy weight popup in order to have event dispatching work
	// correctly.
	Component c = owner;
	while (c != null) {
	    if (c instanceof JComponent) {
		if (((JComponent) c)
			.getClientProperty(forceHeavyWeightPopupKey) == Boolean.TRUE) {
		    popupType1 = HEAVY_WEIGHT_POPUP;
		    break;
		}
	    }
	    c = c.getParent();
	}

	return popupType1;
    }

    /**
     * Obtains the appropriate <code>Popup</code> based on
     * <code>popupType</code>.
     */
    private Popup getPopup(Component owner, Component contents, int ownerX,
	    int ownerY, int popupType1) {
	if (GraphicsEnvironment.isHeadless()) {
	    return getHeadlessPopup(owner, contents, ownerX, ownerY);
	}

	switch (popupType1) {
	case LIGHT_WEIGHT_POPUP:
	    return getLightWeightPopup(owner, contents, ownerX, ownerY);
	case MEDIUM_WEIGHT_POPUP:
	    return getMediumWeightPopup(owner, contents, ownerX, ownerY);
	case HEAVY_WEIGHT_POPUP:
	    return getHeavyWeightPopup(owner, contents, ownerX, ownerY);
	}
	return null;
    }

    /**
     * Creates a headless popup
     */
    private Popup getHeadlessPopup(Component owner, Component contents,
	    int ownerX, int ownerY) {
	return HeadlessPopup.getHeadlessPopup(owner, contents, ownerX, ownerY);
    }

    /**
     * Creates a light weight popup.
     */
    private Popup getLightWeightPopup(Component owner, Component contents,
	    int ownerX, int ownerY) {
	return LightWeightPopup.getLightWeightPopup(owner, contents, ownerX,
		ownerY);
    }

    /**
     * Creates a medium weight popup.
     */
    private Popup getMediumWeightPopup(Component owner, Component contents,
	    int ownerX, int ownerY) {
	return MediumWeightPopup.getMediumWeightPopup(owner, contents, ownerX,
		ownerY);
    }

    /**
     * Creates a heavy weight popup.
     */
    private Popup getHeavyWeightPopup(Component owner, Component contents,
	    int ownerX, int ownerY) {
	if (GraphicsEnvironment.isHeadless()) {
	    return getMediumWeightPopup(owner, contents, ownerX, ownerY);
	}
	return HeavyWeightPopup.getHeavyWeightPopup(owner, contents, ownerX,
		ownerY);
    }

    /**
     * Returns true if the Component <code>i</code> inside a heavy weight
     * <code>Popup</code>.
     */
    private boolean invokerInHeavyWeightPopup(Component i) {
	if (i != null) {
	    Container parent;
	    for (parent = i.getParent(); parent != null; parent = parent
		    .getParent()) {
		if (parent instanceof PieMenuPopup.HeavyWeightWindow) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Popup implementation that uses a Window as the popup.
     */
    private static class HeavyWeightPopup extends PieMenuPopup {
	/**
	 * Returns either a new or recycled <code>Popup</code> containing the
	 * specified children.
	 */
	static Popup getHeavyWeightPopup(Component owner, Component contents,
		int ownerX, int ownerY) {
	    Window window = (owner != null) ? SwingUtilities
		    .getWindowAncestor(owner) : null;
	    HeavyWeightPopup popup = null;

	    if (window != null) {
		popup = getRecycledHeavyWeightPopup(window);
	    }

	    boolean focusPopup = false;
	    if (contents != null && contents.isFocusable()) {
		if (contents instanceof JPopupMenu) {
		    JPopupMenu jpm = (JPopupMenu) contents;
		    Component popComps[] = jpm.getComponents();
		    for (int i = 0; i < popComps.length; i++) {
			if (!(popComps[i] instanceof MenuElement)
				&& !(popComps[i] instanceof JSeparator)) {
			    focusPopup = true;
			    break;
			}
		    }
		}
	    }

	    if (popup == null
		    || ((JWindow) popup.getComponent())
			    .getFocusableWindowState() != focusPopup) {

		if (popup != null) {
		    // The recycled popup can't serve us well
		    // dispose it and create new one
		    popup._dispose();
		}

		popup = new HeavyWeightPopup();
	    }

	    popup.reset(owner, contents, ownerX, ownerY);

	    if (focusPopup) {
		JWindow wnd = (JWindow) popup.getComponent();
		wnd.setFocusableWindowState(true);
		// Set window name. We need this in BasicPopupMenuUI
		// to identify focusable popup window.
		wnd.setName("###focusableSwingPopup###");
	    }

	    return popup;
	}

	/**
	 * Returns a previously disposed heavy weight <code>Popup</code>
	 * associated with <code>window</code>. This will return null if there
	 * is no <code>HeavyWeightPopup</code> associated with
	 * <code>window</code>.
	 */
	private static HeavyWeightPopup getRecycledHeavyWeightPopup(Window w) {
	    synchronized (HeavyWeightPopup.class) {
		List<HeavyWeightPopup> cache;
		if (HeavyWeightPopupCache.containsKey(w)) {
		    cache = HeavyWeightPopupCache.get(w);
		} else {
		    return null;
		}
		if (cache.size() > 0) {
		    HeavyWeightPopup r = cache.get(0);
		    cache.remove(0);
		    return r;
		}
		return null;
	    }
	}

	private static Map<Window, List<HeavyWeightPopup>> HeavyWeightPopupCache = new HashMap<Window, List<HeavyWeightPopup>>(
		2);

	/**
	 * Recycles the passed in <code>HeavyWeightPopup</code>.
	 */
	private static void recycleHeavyWeightPopup(HeavyWeightPopup popup) {
	    synchronized (HeavyWeightPopup.class) {
		List<HeavyWeightPopup> cache;
		Window window = SwingUtilities.getWindowAncestor(popup
			.getComponent());
		if (window instanceof PieMenuPopup.DefaultFrame
			|| !window.isVisible()) {
		    // If the Window isn't visible, we don't cache it as we
		    // likely won't ever get a windowClosed event to clean up.
		    // We also don't cache DefaultFrames as this indicates
		    // there wasn't a valid Window parent, and thus we don't
		    // know when to clean up.
		    popup._dispose();
		    return;
		} else if (HeavyWeightPopupCache.containsKey(window)) {
		    cache = HeavyWeightPopupCache.get(window);
		} else {
		    cache = new ArrayList<HeavyWeightPopup>();
		    HeavyWeightPopupCache.put(window, cache);
		    // Clean up if the Window is closed
		    final Window w = window;

		    w.addWindowListener(new WindowAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void windowClosed(WindowEvent e) {
			    List<HeavyWeightPopup> popups;

			    synchronized (HeavyWeightPopup.class) {
				popups = HeavyWeightPopupCache.remove(w);
			    }
			    if (popups != null) {
				for (int counter = popups.size() - 1; counter >= 0; counter--) {
				    popups.get(counter)._dispose();
				}
			    }
			}
		    });
		}

		if (cache.size() < MAX_CACHE_SIZE) {
		    cache.add(popup);
		} else {
		    popup._dispose();
		}
	    }
	}

	//
	// Popup methods
	//
	@Override
	public void hide() {
	    super.hide();
	    recycleHeavyWeightPopup(this);
	}

	/**
	 * As we recycle the <code>Window</code>, we don't want to dispose it,
	 * thus this method does nothing, instead use <code>_dipose</code> which
	 * will handle the disposing.
	 */
	@Override
	void dispose() {
	}

	void _dispose() {
	    super.dispose();
	}
    }

    /**
     * ContainerPopup consolidates the common code used in the light/medium
     * weight implementations of <code>Popup</code>.
     */
    private static class ContainerPopup extends PieMenuPopup {
	/** Component we are to be added to. */
	Component owner;
	/** Desired x location. */
	int x;
	/** Desired y location. */
	int y;

	@Override
	public void hide() {
	    Component component = getComponent();

	    if (component != null) {
		Container parent = component.getParent();

		if (parent != null) {
		    Rectangle bounds = component.getBounds();

		    parent.remove(component);
		    parent.repaint(bounds.x, bounds.y, bounds.width,
			    bounds.height);
		}
	    }
	    this.owner = null;
	}

	@Override
	public void pack() {
	    Component component = getComponent();

	    if (component != null) {
		component.setSize(component.getPreferredSize());
	    }
	}

	@Override
	void reset(Component owner1, Component contents, int ownerX, int ownerY) {
	    if ((owner1 instanceof JFrame) || (owner1 instanceof JDialog)
		    || (owner1 instanceof JWindow)) {
		// Force the content to be added to the layered pane, otherwise
		// we'll get an exception when adding to the RootPaneContainer.
		owner1 = ((RootPaneContainer) owner1).getLayeredPane();
	    }
	    super.reset(owner1, contents, ownerX, ownerY);

	    this.x = ownerX - getComponent().getWidth() / 2;
	    this.y = ownerY - getComponent().getHeight() / 2;
	    this.owner = owner1;
	}

	boolean overlappedByOwnedWindow() {
	    Component component = getComponent();
	    if (this.owner != null && component != null) {
		Window w = SwingUtilities.getWindowAncestor(this.owner);
		if (w == null) {
		    return false;
		}
		Window[] ownedWindows = w.getOwnedWindows();
		if (ownedWindows != null) {
		    Rectangle bnd = component.getBounds();
		    for (int i = 0; i < ownedWindows.length; i++) {
			Window owned = ownedWindows[i];
			if (owned.isVisible()
				&& bnd.intersects(owned.getBounds())) {

			    return true;
			}
		    }
		}
	    }
	    return false;
	}

	/**
	 * Returns true if the Popup can fit on the screen.
	 */
	boolean fitsOnScreen() {
	    Component component = getComponent();

	    if (this.owner != null && component != null) {
		Container parent;
		int width = component.getWidth();
		int height = component.getHeight();
		for (parent = this.owner.getParent(); parent != null; parent = parent
			.getParent()) {
		    if (parent instanceof JFrame || parent instanceof JDialog
			    || parent instanceof JWindow) {

			Rectangle r = parent.getBounds();
			Insets i = parent.getInsets();
			r.x += i.left;
			r.y += i.top;
			r.width -= (i.left + i.right);
			r.height -= (i.top + i.bottom);

			GraphicsConfiguration gc = parent
				.getGraphicsConfiguration();
			Rectangle popupArea = getContainerPopupArea(gc);
			return r.intersection(popupArea).contains(this.x,
				this.y, width, height);

		    } else if (parent instanceof JApplet) {
			Rectangle r = parent.getBounds();
			Point p = parent.getLocationOnScreen();

			r.x = p.x;
			r.y = p.y;
			return r.contains(this.x, this.y, width, height);
		    } else if (parent instanceof Window
			    || parent instanceof Applet) {
			// No suitable swing component found
			break;
		    }
		}
	    }
	    return false;
	}

	Rectangle getContainerPopupArea(GraphicsConfiguration gc) {
	    Rectangle screenBounds;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Insets insets;
	    if (gc != null) {
		// If we have GraphicsConfiguration use it
		// to get screen bounds
		screenBounds = gc.getBounds();
		insets = toolkit.getScreenInsets(gc);
	    } else {
		// If we don't have GraphicsConfiguration use primary screen
		screenBounds = new Rectangle(toolkit.getScreenSize());
		insets = new Insets(0, 0, 0, 0);
	    }
	    // Take insets into account
	    screenBounds.x += insets.left;
	    screenBounds.y += insets.top;
	    screenBounds.width -= (insets.left + insets.right);
	    screenBounds.height -= (insets.top + insets.bottom);
	    return screenBounds;
	}
    }

    /**
     * Popup implementation that is used in headless environment.
     */
    @SuppressWarnings("synthetic-access")
    private static class HeadlessPopup extends ContainerPopup {
	static Popup getHeadlessPopup(Component owner, Component contents,
		int ownerX, int ownerY) {
	    HeadlessPopup popup = new HeadlessPopup();
	    popup.reset(owner, contents, ownerX, ownerY);
	    return popup;
	}

	@Override
	Component createComponent(Component owner1) {
	    return new Panel(new BorderLayout());
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}
    }

    /**
     * Popup implementation that uses a JPanel as the popup.
     */
    @SuppressWarnings("synthetic-access")
    private static class LightWeightPopup extends ContainerPopup {
	/**
	 * Returns a light weight <code>Popup</code> implementation. If the
	 * <code>Popup</code> needs more space that in available in
	 * <code>owner</code>, this will return null.
	 */
	static Popup getLightWeightPopup(Component owner, Component contents,
		int ownerX, int ownerY) {
	    LightWeightPopup popup = getRecycledLightWeightPopup();

	    if (popup == null) {
		popup = new LightWeightPopup();
	    }
	    popup.reset(owner, contents, ownerX, ownerY);
	    if (!popup.fitsOnScreen() || popup.overlappedByOwnedWindow()) {
		popup.hide();
		return null;
	    }
	    return popup;
	}

	private static List<LightWeightPopup> LightWeightPopupCache = new ArrayList<LightWeightPopup>();

	/**
	 * Recycles the LightWeightPopup <code>popup</code>.
	 */
	private static void recycleLightWeightPopup(LightWeightPopup popup) {
	    synchronized (LightWeightPopup.class) {
		if (LightWeightPopupCache.size() < MAX_CACHE_SIZE) {
		    LightWeightPopupCache.add(popup);
		}
	    }
	}

	/**
	 * Returns a previously used <code>LightWeightPopup</code>, or null if
	 * none of the popups have been recycled.
	 */
	private static LightWeightPopup getRecycledLightWeightPopup() {
	    synchronized (LightWeightPopup.class) {
		if (LightWeightPopupCache.size() > 0) {
		    LightWeightPopup r = LightWeightPopupCache.get(0);
		    LightWeightPopupCache.remove(0);
		    return r;
		}
		return null;
	    }
	}

	//
	// Popup methods
	//
	@Override
	public void hide() {
	    super.hide();
	    Container component = (Container) getComponent();
	    component.removeAll();
	    recycleLightWeightPopup(this);
	}

	@Override
	public void show() {
	    Container parent = null;

	    if (this.owner != null) {
		parent = (this.owner instanceof Container ? (Container) this.owner
			: this.owner.getParent());
	    }

	    // Try to find a JLayeredPane and Window to add
	    for (Container p = parent; p != null; p = p.getParent()) {
		if (p instanceof JRootPane) {
		    if (p.getParent() instanceof JInternalFrame) {
			continue;
		    }
		    parent = ((JRootPane) p).getLayeredPane();
		    // Continue, so that if there is a higher JRootPane, we'll
		    // pick it up.
		} else if (p instanceof Window) {
		    if (parent == null) {
			parent = p;
		    }
		    break;
		} else if (p instanceof JApplet) {
		    // Painting code stops at Applets, we don't want
		    // to add to a Component above an Applet otherwise
		    // you'll never see it painted.
		    break;
		}
	    }

	    if (parent == null) {
		return;
	    }

	    Point p = convertScreenLocationToParent(parent, this.x, this.y);
	    Component component = getComponent();

	    component.setLocation(p.x, p.y);
	    if (parent instanceof JLayeredPane) {
		((JLayeredPane) parent).add(component,
			JLayeredPane.POPUP_LAYER, 0);
	    } else {
		parent.add(component);
	    }
	}

	@Override
	Component createComponent(Component owner1) {
	    JComponent component = new JPieMenue();
	    component.setOpaque(true);
	    return component;
	}

	//
	// Local methods
	//

	/**
	 * Resets the <code>Popup</code> to an initial state.
	 */
	@Override
	void reset(Component owner1, Component contents, int ownerX, int ownerY) {
	    super.reset(owner1, contents, ownerX, ownerY);
	    JComponent component = (JComponent) getComponent();
	    component.setOpaque(contents.isOpaque());
	    component.setLocation(ownerX - component.getWidth() / 2, ownerY
		    - component.getHeight() / 2);
	    component.add(contents, BorderLayout.CENTER);
	    contents.invalidate();
	    pack();
	}
    }

    /**
     * Converts the location <code>x</code> <code>y</code> to the parents
     * coordinate system, returning the location.
     */
    static Point convertScreenLocationToParent(Container parent, int x, int y) {
	for (Container p = parent; p != null; p = p.getParent()) {
	    if (p instanceof Window) {
		Point point = new Point(x, y);

		SwingUtilities.convertPointFromScreen(point, parent);
		return point;
	    }
	}
	throw new Error("convertScreenLocationToParent: no window ancestor");
    }

    /**
     * Popup implementation that uses a Panel as the popup.
     */
    @SuppressWarnings("synthetic-access")
    private static class MediumWeightPopup extends ContainerPopup {
	/** Child of the panel. The contents are added to this. */
	private JRootPane rootPane;

	/**
	 * Returns a medium weight <code>Popup</code> implementation. If the
	 * <code>Popup</code> needs more space that in available in
	 * <code>owner</code>, this will return null.
	 */
	static Popup getMediumWeightPopup(Component owner, Component contents,
		int ownerX, int ownerY) {
	    MediumWeightPopup popup = getRecycledMediumWeightPopup();

	    if (popup == null) {
		popup = new MediumWeightPopup();
	    }
	    popup.reset(owner, contents, ownerX, ownerY);
	    if (!popup.fitsOnScreen() || popup.overlappedByOwnedWindow()) {
		popup.hide();
		return null;
	    }
	    return popup;
	}

	private static List<MediumWeightPopup> MediumWeightPopupCache = new ArrayList<MediumWeightPopup>();

	/**
	 * Recycles the MediumWeightPopupCache <code>popup</code>.
	 */
	private static void recycleMediumWeightPopup(MediumWeightPopup popup) {
	    synchronized (MediumWeightPopup.class) {
		if (MediumWeightPopupCache.size() < MAX_CACHE_SIZE) {
		    MediumWeightPopupCache.add(popup);
		}
	    }
	}

	/**
	 * Returns a previously used <code>MediumWeightPopupCache</code>, or
	 * null if none of the popups have been recycled.
	 */
	private static MediumWeightPopup getRecycledMediumWeightPopup() {
	    synchronized (MediumWeightPopup.class) {
		if (MediumWeightPopupCache.size() > 0) {
		    MediumWeightPopup r = MediumWeightPopupCache.get(0);
		    MediumWeightPopupCache.remove(0);
		    return r;
		}
		return null;
	    }
	}

	//
	// Popup
	//

	@Override
	public void hide() {
	    super.hide();
	    this.rootPane.getContentPane().removeAll();
	    recycleMediumWeightPopup(this);
	}

	@Override
	public void show() {
	    Component component = getComponent();
	    Container parent = null;

	    if (this.owner != null) {
		parent = this.owner.getParent();
	    }
	    /*
	     * Find the top level window, if it has a layered pane, add to that,
	     * otherwise add to the window.
	     */
	    while (!(parent instanceof Window || parent instanceof Applet)
		    && (parent != null)) {
		parent = parent.getParent();
	    }
	    // Set the visibility to false before adding to workaround a
	    // bug in Solaris in which the Popup gets added at the wrong
	    // location, which will result in a mouseExit, which will then
	    // result in the ToolTip being removed.
	    if (parent instanceof RootPaneContainer) {
		parent = ((RootPaneContainer) parent).getLayeredPane();
		Point p = convertScreenLocationToParent(parent, this.x, this.y);
		component.setVisible(false);
		component.setLocation(p.x, p.y);
		((JLayeredPane) parent).add(component,
			JLayeredPane.POPUP_LAYER, 0);
	    } else {
		Point p = convertScreenLocationToParent(parent, this.x, this.y);

		component.setLocation(p.x, p.y);
		component.setVisible(false);
		parent.add(component);
	    }
	    component.setVisible(true);
	}

	@Override
	Component createComponent(Component owner1) {
	    Panel component = new MediumWeightComponent();

	    this.rootPane = new JRootPane();
	    // NOTE: this uses setOpaque vs LookAndFeel.installProperty as
	    // there is NO reason for the RootPane not to be opaque. For
	    // painting to work the contentPane must be opaque, therefor the
	    // RootPane can also be opaque.
	    this.rootPane.setOpaque(true);
	    component.add(this.rootPane, BorderLayout.CENTER);
	    return component;
	}

	/**
	 * Resets the <code>Popup</code> to an initial state.
	 */
	@Override
	void reset(Component owner1, Component contents, int ownerX, int ownerY) {
	    super.reset(owner1, contents, ownerX, ownerY);

	    Component component = getComponent();

	    component.setLocation(ownerX, ownerY);
	    this.rootPane.getContentPane().add(contents, BorderLayout.CENTER);
	    contents.invalidate();
	    component.validate();
	    pack();
	}

	// This implements SwingHeavyWeight so that repaints on it
	// are processed by the RepaintManager and SwingPaintEventDispatcher.
	private static class MediumWeightComponent extends Panel
	// implements
	// SwingHeavyWeight
	{
	    /**
	     * {@link long} COMMENT.
	     */
	    private static final long serialVersionUID = -2236274587371892578L;

	    MediumWeightComponent() {
		super(new BorderLayout());
	    }
	}
    }
}
