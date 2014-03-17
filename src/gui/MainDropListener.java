package gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

public class MainDropListener implements DropTargetListener
{
  private MainPanel mP;

  public MainDropListener( final MainPanel mP )
  {
    this.mP = mP;
  }

  @Override
  public void dragEnter( final DropTargetDragEvent e ) {}

  @Override
  public void dragOver( final DropTargetDragEvent e ) {}

  @Override
  public void dropActionChanged( final DropTargetDragEvent e ) {}

  @Override
  public void dragExit( final DropTargetEvent e ) {}

  @SuppressWarnings("unchecked")
  @Override
  public void drop( final DropTargetDropEvent e )
  {
    final Transferable tr = e.getTransferable();

    if ( tr.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) )
    {
      try
      {
        e.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK );

        for ( final File file : (List<File>) tr.getTransferData( DataFlavor.javaFileListFlavor ) )
        {
          mP.enqueue( file.getAbsolutePath() );
        }
      }
      catch ( final Throwable t )
      {
        t.printStackTrace();
      }
    }
  }
}
