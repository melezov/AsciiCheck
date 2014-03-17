package gui;

import io.Foil;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import cnv.PS2PDF;

public class MainPanel extends JTabbedPane
{
  public final List<Foil> foilList;
  private final DropTargetListener dTL;

  public MainPanel()
  {
    new DropTarget( this, dTL = new MainDropListener( this ) );
    foilList = Collections.synchronizedList( new ArrayList<Foil>() );
  }

  public void enqueue( final String path )
  {
    if ( !Foil.check( path ) )
    {
      JOptionPane.showMessageDialog( this, "Could not read file \"" + path + "\"!" );
      return;
    }

    try
    {
      final FoilTab fT = new FoilTab( dTL );

      final Foil f = new Foil( path, this, fT );
      foilList.add( f );

      final JScrollPane jSP = new JScrollPane( fT,
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

      final int index = foilList.indexOf( f );
      insertTab( f.getLabel(), null, jSP, "Open " + f.name, index );

      new Thread( f ).start();
    }
    catch ( final Throwable t )
    {
      t.printStackTrace();
    }
  }

  private static final long serialVersionUID = "AsciiCheck:gui.MainPanel".hashCode();
}
