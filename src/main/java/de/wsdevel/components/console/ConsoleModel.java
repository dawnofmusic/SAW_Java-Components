package de.wsdevel.components.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import de.wsdevel.tools.awt.model.observer.ObserverList;
import de.wsdevel.tools.streams.TeeOutputStream;

/**
 * Created on 26.04.2004.
 * 
 * for project: tools
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2005/10/31 19:11:33
 *          $
 * 
 * <br>
 *          (c) 2005, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public final class ConsoleModel {

    /**
     * the instance of this singleton.
     */
    private static ConsoleModel instance = null;

    /**
     * {@link ObserverList}listeners to this model.
     */
    private ObserverList<ConsoleModelListener> listeners = new ObserverList<ConsoleModelListener>();

    /**
     * {@link ConsoleOutputStream}.
     */
    private ConsoleOutputStream consoleOut;

    /**
     * {@link ConsoleOutputStream}.
     */
    private ConsoleOutputStream consoleErr;

    /**
     * @param teeSystemOut
     *            <code>boolean</code>
     */
    public static void initialize(final boolean teeSystemOut) {
	if (instance == null) {
	    instance = new ConsoleModel(teeSystemOut);
	}
    }

    /**
     * @return {@link ConsoleModel} the instance
     */
    public static ConsoleModel getInstance() {
	if (instance == null) {
	    throw new IllegalStateException(
		    "Not yet initialized! Use ConsoleModel.initialize() first!");
	}
	return instance;
    }

    /**
     * @param teeSystemOut
     *            <code>boolean</code>
     */
    private ConsoleModel(final boolean teeSystemOut) {
	this.consoleOut = new ConsoleOutputStream(new Writer() {
	    public void write(final String string) {
		ConsoleModel.this.listeners
			.observe(new ObserverList.Action<ConsoleModelListener>() {
			    public void doit(final ConsoleModelListener listener) {
				listener.out(string);
			    }
			});
	    }
	});
	this.consoleErr = new ConsoleOutputStream(new Writer() {
	    public void write(final String string) {
		ConsoleModel.this.listeners
			.observe(new ObserverList.Action<ConsoleModelListener>() {
			    public void doit(final ConsoleModelListener listener) {
				listener.err(string);
			    }
			});
	    }
	});
	if (teeSystemOut) {
	    System.setOut(new PrintStream(new TeeOutputStream(this.consoleOut,
		    System.out)));
	    System.setErr(new PrintStream(new TeeOutputStream(this.consoleErr,
		    System.err)));
	}
    }

    /**
     * Created on 03.10.2005.
     * 
     * for project: tools
     * 
     * @author <a href="mailto:weiss@dawnofmusic.com">Sebastian A. Weiss - dawn
     *         of music</a>
     * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2005/10/03
     *          18:26:00 $
     * 
     *          (c) dawn of music 2004 - All rights reserved.
     * 
     */
    public static interface ConsoleModelListener {

	/**
	 * @param value
	 *            {@link String}
	 */
	void out(String value);

	/**
	 * @param value
	 *            {@link String}
	 */
	void err(String value);
    }

    /**
     * @param listener
     *            {@link ConsoleModelListener}
     */
    public void addConsoleModelListener(final ConsoleModelListener listener) {
	this.listeners.addListener(listener);
    }

    /**
     * @param listener
     *            {@link ConsoleModelListener}
     */
    public void removeConsoleModelListener(final ConsoleModelListener listener) {
	this.listeners.removeListener(listener);
    }

    /**
     * Created on 03.10.2005.
     * 
     * for project: tools
     * 
     * @author <a href="mailto:weiss@dawnofmusic.com">Sebastian A. Weiss - dawn
     *         of music</a>
     * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2005/10/03
     *          18:26:00 $
     * 
     *          (c) dawn of music 2004 - All rights reserved.
     * 
     */
    public static interface Writer {

	/**
	 * @param string
	 *            {@link String}
	 */
	void write(String string);
    }

    /**
     * Created on 03.10.2005.
     * 
     * for project: tools
     * 
     * @author <a href="mailto:weiss@dawnofmusic.com">Sebastian A. Weiss - dawn
     *         of music</a>
     * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2005/10/03
     *          18:26:00 $
     * 
     *          (c) dawn of music 2004 - All rights reserved.
     * 
     */
    public static class ConsoleOutputStream extends OutputStream {

	/**
	 * we keep a buffer around for creating 1-char strings, to avoid the
	 * potential horror of thousads of array allocations per second.
	 * 
	 * <code>byte[]</code>
	 */
	private byte[] littlebuf = new byte[1];

	/**
	 * {@link Writer}.
	 */
	private Writer writer = null;

	/**
	 * @param writerVal
	 *            {@link Writer}
	 */
	public ConsoleOutputStream(final Writer writerVal) {
	    this.writer = writerVal;
	}

	/**
	 * @param b
	 *            <code>int</code>
	 * @throws IOException
	 *             COMMENT
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public final void write(final int b) throws IOException {
	    this.littlebuf[0] = (byte) b;
	    this.writer.write(new String(this.littlebuf, 0, 1));
	}

	/**
	 * @param b
	 *            <code>byte[]</code>
	 * @param off
	 *            <code>int</code>
	 * @param len
	 *            <code>int</code>
	 * @throws IOException
	 *             COMMENT
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public final void write(final byte[] b, final int off, final int len)
		throws IOException {
	    this.writer.write(new String(b, off, len));
	}

	/**
	 * @param b
	 *            <code>byte[]</code>
	 * @throws IOException
	 *             COMMENT
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public final void write(final byte[] b) throws IOException {
	    this.writer.write(new String(b, 0, b.length));
	}

	/**
	 * @throws IOException
	 *             COMMENT
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public final void close() throws IOException {
	    // do nothing (sw)
	}

	/**
	 * @throws IOException
	 *             COMMENT
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public final void flush() throws IOException {
	    // do nothing (sw)
	}
    }

    /**
     * @return {@link ConsoleOutputStream}
     */
    public ConsoleOutputStream getConsoleErr() {
	return this.consoleErr;
    }

    /**
     * @return {@link ConsoleOutputStream}
     */
    public ConsoleOutputStream getConsoleOut() {
	return this.consoleOut;
    }

}
/*
 * $Log: ConsoleModel.java,v $ Revision 1.3 2007-08-03 11:14:24 sweiss *** empty
 * log message ***
 * 
 * Revision 1.2 2006/06/10 13:00:33 sweissTFH cleanup and smaller changes due to
 * new compiler settings
 * 
 * Revision 1.1 2006/05/02 16:06:01 sweissTFH cleaned up tools and moved
 * everything to appropriate new packages
 * 
 * Revision 1.6 2006/04/05 18:19:35 sweissTFH cleaned up checkstyle errors
 * 
 * Revision 1.5 2005/12/27 16:06:01 sweissTFH moved to java 5 and very big clean
 * up!
 * 
 * Revision 1.4 2005/10/31 19:11:33 sweissTFH cleaned up and commented a bit
 * 
 * Revision 1.3 2005/10/03 18:26:00 sweissTFH changes in JConsole; it became
 * more preformant as well cleaned up and added comments!
 */
