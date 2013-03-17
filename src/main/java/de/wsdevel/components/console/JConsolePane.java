package de.wsdevel.components.console;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created on 26.04.2004.
 * 
 * for project: tools
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: ischmidt $ -- $Revision: 1.3 $ -- $Date: 2005/10/31
 *          19:11:33 $
 * 
 * <br>
 *          (c) 2005, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class JConsolePane extends JTextPane implements
	ConsoleModel.ConsoleModelListener {

    /**
     * serial version unique id.
     */
    private static final long serialVersionUID = -4457016416164742524L;

    /**
     * logger.
     */
    private static final Log LOG = LogFactory.getLog(JConsolePane.class);

    /**
     * default constructor.
     */
    public JConsolePane() {
	super();
	setEditable(false);
	ConsoleModel.getInstance().addConsoleModelListener(this);
	setContentType("text/plain");
	setDocument(new DefaultStyledDocument());
    }

    /**
     * @param value
     *            {@link String}
     * @see de.wsdevel.tools.awt.components.console.ConsoleModel.ConsoleModelListener#out(java.lang.String)
     */
    public final void out(final String value) {
	// setText(getText() + value);
	try {
	    boolean isCaretAtDocumentEnd = (getDocument().getLength() == getCaretPosition());
	    getDocument().insertString(getDocument().getLength(), value, null);
	    if (isCaretAtDocumentEnd) {
		setCaretPosition(getDocument().getLength());
	    }
	} catch (BadLocationException e) {
	    LOG.error("bad insert location", e);
	}
    }

    /**
     * @param value
     *            {@link String}
     * @see de.wsdevel.tools.awt.components.console.ConsoleModel.ConsoleModelListener#err(java.lang.String)
     */
    public final void err(final String value) {
	// setText(getText() + value);
	try {
	    boolean isCaretAtDocumentEnd = getDocument().getLength() == getCaretPosition();
	    getDocument().insertString(getDocument().getLength(), value, null);
	    if (isCaretAtDocumentEnd) {
		setCaretPosition(getDocument().getLength());
	    }
	} catch (BadLocationException e) {
	    LOG.error("bad insert location", e);
	}
    }

}
/*
 * $ Log: $
 */
