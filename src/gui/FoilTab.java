package gui;

import io.Foil;
import io.Line;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;

import javax.swing.JEditorPane;

public class FoilTab extends JEditorPane
{
  public FoilTab( final DropTargetListener dTL )
  {
    super( "text/html", null );
    setEditable( false );

    setText( "<html><h2>Loading ...</h2></html>" );
    setDropTarget( new DropTarget( this, dTL ) );
  }

  public void setBody( final Foil f )
  {
    final StringBuilder sB = new StringBuilder();

    final int count = f.size();
    final int digits = String.valueOf( count ).length();

    sB.append( "<html>" );
    sB.append( "<pre>" );

    final int eC = f.getErrorCount();
    int index = 0;

    if ( eC > 0 )
    {
      sB.append( "<font color=\"blue\">Errors found on <font color=\"red\">" + eC + "</font> lines:<br><br>" );

      for ( final Line l : f )
      {
        index ++;

        int cnt = l.errMap.size();
        if ( cnt == 0 ) continue;

        sB.append( "\tLine <font color=\"red\">" )
        .append( index )
        .append( "</font>: column " );

        boolean newError = true;

        for ( final int i : l.errMap.keySet() )
        {
          sB.append( newError ? "" : "<font color=\"black\">, </font>" )
          .append( "<font color=\"red\">" )
          .append( i + 1 )
          .append( "</font>" );

          newError = false;
        }

        sB.append( "<br>" );
      }

      sB.append( "<br></font></pre><hr><pre>" );
    }

    index = 0;

    for ( final Line l : f )
    {
      index ++;

      final boolean ok = l.errMap.isEmpty();

      if ( !ok )
        sB.append( "<font color=\"red\">" );

      sB.append( String.format( "%" + digits + "d: ", index ) );

      if ( ok )
        sB.append( "<font color=\"green\">" );

      for ( int i = 0; i < l.bytes.length; i ++ )
      {
        esc( sB, l.bytes[ i ] );
      }

      sB.append( "</font>" );
      sB.append( "<br>" );
    }

    sB.append( "</pre>" );
    sB.append( "</html>" );

    setText( sB.toString() );
    setCaretPosition( 0 );
  }

  public static void esc( final StringBuilder sB, final byte b )
  {
    switch( b )
    {
      case ' ':   sB.append( "&nbsp;" ); break;
      case '\t':   sB.append( "&nbsp;&nbsp;&nbsp;&nbsp;" ); break;
      case '&':  sB.append( "&amp;" ); break;
      case '<':    sB.append( "&lt;" ); break;
      case '>':   sB.append( "&gt;" ); break;

      default:  sB.append( (char) b );
    }
  }

  private static final long serialVersionUID = "AsciiCheck:gui.FoilTab".hashCode();
}
