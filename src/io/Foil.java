package io;

import gui.FoilTab;
import gui.MainPanel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cnv.PS2PDF;

public class Foil extends ArrayList<Line> implements Runnable, Echoable
{
  private static byte[] readFile( final String path ) throws IOException
  {
    final File file = new File( path );
    final FileInputStream fIS = new FileInputStream( file );
    final byte[] buf = new byte[ (int) file.length() ];
    fIS.read( buf );
    fIS.close();

    return buf;
  }

  private final MainPanel mP;
  private final FoilTab fT;

  public final String path;
  public final String name;

  public static boolean check( final String path )
  {
    try
    {
      final File file = new File( path );
      return file.exists() && file.isFile() && file.canRead();
    }
    catch ( final Throwable t )
    {
      t.printStackTrace();
    }

    return false;
  }

  public Foil( final String path, final MainPanel mP, final FoilTab fT ) throws Throwable
  {
    final File file = new File( path );

    this.path = file.getAbsolutePath();
    this.name = file.getName();
    this.mP = mP;
    this.fT = fT;
  }

  private static final Pattern PS_FILE = Pattern.compile( "(.*?)(\\.ps)$" );

  public void run()
  {
    final Matcher m = PS_FILE.matcher( path );
    if ( m.find() )
    {
      new PS2PDF( this ).processFile( path, m.group( 1 ) + ".pdf" );
      fT.setBody( this );
      return;
    }

    try
    {
      clear();

      final byte[] buf = readFile( path );

      final ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
      Terminator lineTerm = Terminator.NO;

      for ( int i = 0; i < buf.length; i ++ )
      {
        final byte b = buf[ i ];

        if ( lineTerm.isNewLine( b ) )
        {
          add( new Line( bAOS.toByteArray(), lineTerm ) );
          bAOS.reset();
        }

        lineTerm = lineTerm.evolve( b );

        if ( lineTerm == Terminator.NO )
          bAOS.write( b );
      }

      add( new Line( bAOS.toByteArray(), lineTerm ) );
      bAOS.close();

      final int index = mP.foilList.indexOf( this );

      mP.setTitleAt( index, getLabel() );
      fT.setBody( this );
    }
    catch ( final Exception e )
    {
      e.printStackTrace();
    }
  }

  public int getErrorCount( )
  {
    int errorCount = 0;

    for ( final Line l : this )
      if ( !l.errMap.isEmpty() ) errorCount ++;

    return errorCount;
  }

  public String getLabel()
  {
    final int eC = getErrorCount();

    final StringBuilder sB = new StringBuilder();

    sB.append( "<html>" )
      .append( name )
      .append( " (<font color=\"" )
        .append( eC > 0 ? "red" : "green" )
      .append( "\">" )
      .append( eC )
      .append( "/" )
      .append( size() )
    .append( ")</font></html>" );

    return sB.toString();
  }

  private static final long serialVersionUID = "AsciiCheck:io.Foil".hashCode();

  @Override
  public void echo( final String message )
  {
    try
    {
      add( new Line( message.getBytes( "UTF-8" ), Terminator.PC ) );
    }
    catch ( final Throwable t ){}

    fT.setBody( this );
  }
}
