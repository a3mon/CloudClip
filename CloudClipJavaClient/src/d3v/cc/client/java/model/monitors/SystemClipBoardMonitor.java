package d3v.cc.client.java.model.monitors;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemClipBoardMonitor implements ClipboardOwner, ISystemClipBoardMonitor {

    private static final int                     CLIPBOARD_ACCESS_MAX    = 10;
    private static final int                     CLIPBOARD_ACCESS_DELAY  = 100;
    private static final Logger                  log                     = Logger.getLogger( SystemClipBoardMonitor.class.getName( ) );
    private final List<ISystemClipBoardListener> systemClipBoardListener;

    private int                                  failedClipboardAccesses = 0;
    private String                               clip;

    public SystemClipBoardMonitor( ) {
        this.systemClipBoardListener = new ArrayList<ISystemClipBoardListener>( );

        Clipboard sysClip = Toolkit.getDefaultToolkit( ).getSystemClipboard( );
        gainOwnership( sysClip );
    }

    @Override
    public void lostOwnership( Clipboard clipboard, Transferable content ) {
        log.finest( "Lost OwnerShip" );
        processContent( clipboard );
        gainOwnership( clipboard );
    }

    private void processContent( Clipboard clipboard ) {
        try {
        	String oldClip = this.clip;
            this.clip = clipboard.getData( DataFlavor.stringFlavor ).toString( );
            log.finest( String.format("oldClip: %s, clip: %s", oldClip, this.clip ) );
            
            if(!this.clip.equals(oldClip)) {
            	log.finer( String.format("Found new clip: %s", this.clip) );
            	notifySystemClipBoardListener( this.clip );
            }
            
        } catch ( IllegalStateException e ) {
            failedClipboardAccesses++ ;

            if ( failedClipboardAccesses == CLIPBOARD_ACCESS_MAX ) {
                log.severe( "Can not access Clipboard at " + failedClipboardAccesses + " attempt. Giving up now" );
                return;
            }

            log.finer( "Can not access Clipboard at " + failedClipboardAccesses + " attempt. Try again in " + CLIPBOARD_ACCESS_DELAY + " ms" );
            try {
                Thread.sleep( CLIPBOARD_ACCESS_DELAY );
                processContent( clipboard );
            } catch ( InterruptedException e1 ) {
                log.log( Level.SEVERE, e1.getMessage( ), e1 );
            }
        } catch ( UnsupportedFlavorException e ) {
            log.log( Level.SEVERE, e.getMessage( ), e );
        } catch ( IOException e ) {
            log.log( Level.SEVERE, e.getMessage( ), e );
        }
    }

    private void gainOwnership( Clipboard clipboard ) {
        log.entering( SystemClipBoardMonitor.class.getName( ), "gainOwnership" );
        try {
            String clip = clipboard.getData( DataFlavor.stringFlavor ).toString( );
            clipboard.setContents( new StringSelection( clip ), this );
        } catch ( IllegalStateException e ) {
            failedClipboardAccesses++ ;

            if ( failedClipboardAccesses == CLIPBOARD_ACCESS_MAX ) {
                log.severe( "Can not access Clipboard at " + failedClipboardAccesses + " attempt. Giving up now" );
                return;
            }

            log.finer( "Can not access Clipboard at " + failedClipboardAccesses + " attempt. Try again in " + CLIPBOARD_ACCESS_DELAY + " ms" );
            try {
                Thread.sleep( CLIPBOARD_ACCESS_DELAY );
                gainOwnership( clipboard );
            } catch ( InterruptedException e1 ) {
                log.log( Level.SEVERE, e1.getMessage( ), e1 );
            }
        } catch ( UnsupportedFlavorException e ) {
            log.log( Level.SEVERE, e.getMessage( ), e );
        } catch ( IOException e ) {
            log.log( Level.SEVERE, e.getMessage( ), e );
        }

        failedClipboardAccesses = 0;
        log.exiting( SystemClipBoardMonitor.class.getName( ), "gainOwnership" );
    }

    /** 
     * @see d3v.cc.client.java.model.monitors.ISystemClipBoardMonitor#
     * addSystemClipBoardListener
     * (d3v.cc.client.java.model.monitors.ISystemClipBoardListener)
     */
    @Override
    public boolean addSystemClipBoardListener( ISystemClipBoardListener listener ) {
        return this.systemClipBoardListener.add( listener );
    }

    /**
     * @see d3v.cc.client.java.model.monitors.ISystemClipBoardMonitor#
     * removeSystemClipBoardListener
     * (d3v.cc.client.java.model.monitors.ISystemClipBoardListener)
     */
    @Override
    public boolean removeSystemClipBoardListener( ISystemClipBoardListener listener ) {
        return this.systemClipBoardListener.remove( listener );
    }

    private void notifySystemClipBoardListener( String clip ) {
        for ( ISystemClipBoardListener listener : this.systemClipBoardListener ) {
            listener.systemClipBoardChanged( clip );
        }
    }

}
