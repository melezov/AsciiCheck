package cnv;

import io.Echoable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PS2PDF
{
  private static final String PS2PDF_URL = "https://secure.ele-math.com/ps2pdf";
  private static final String TOKEN = "EMPS2PDFv00a";

  private final Echoable eC;

  public PS2PDF( final Echoable eC )
  {
    this.eC = eC;
  }

  private byte[] convert( final byte[] ps )
  {
    try
    {
      final URL url = new URL( PS2PDF_URL );

      eC.echo( "Connecting to Ele-Math.com TeX server ..." );

      final URLConnection uC = url.openConnection();
      uC.setDoInput( true );
      uC.setDoOutput( true );
      uC.setAllowUserInteraction( false );

      final String width = "100";
      final String height = "100";

      final String params = "token="+TOKEN+"&width="+width+"&height="+height+"&file=";

      final ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
      bAOS.write( params.getBytes( "UTF-8" ) );

      final char[] hex = "0123456789ABCDEF".toCharArray();

      for ( final byte b : ps )
      {
        bAOS.write( hex[ ( b >> 4 ) & 0xf ] );
        bAOS.write( hex[ b & 0xf ] );
      }

      uC.getOutputStream().write( bAOS.toByteArray() );

      eC.echo( "File transmitted! Waiting for TeX to finish ..." );

        final InputStream iS = uC.getInputStream();
        final byte[] body = new byte[ 65536 ];
      bAOS.reset();

        while ( true )
        {
          final int read = iS.read( body );
          if ( read == -1 ) break;

          bAOS.write( body, 0, read );
        }

        eC.echo( "Closing connection ..." );

        final byte[] pdf = bAOS.toByteArray();
        return pdf;
    }
    catch ( final Throwable t )
    {
      t.printStackTrace();
      return null;
    }
  }

  private byte[] read( final String path )
  {
    try
    {
      final File file = new File( path );
      final int size = (int) file.length();

      final String fullPath = file.getCanonicalPath();

      eC.echo( "Reading \"" + fullPath + "\" ... " );

      final byte[] body = new byte[ size ];
      final FileInputStream fIS = new FileInputStream( file );
      final int read = fIS.read( body );
      fIS.close();

      if ( read != size ) throw new IOException( "Could not read the entire file!" );

      eC.echo( "Read " + size + " bytes! (OK)" );
      return body;
    }
    catch( final Throwable t )
    {
      t.printStackTrace();
      return null;
    }
  }

  private boolean write( final String path, final byte[] pdf )
  {
    try
    {
      final File file = new File( path );
      final int size = pdf.length;

      final String fullPath = file.getCanonicalPath();

      eC.echo( "Writing \"" + fullPath + "\" ... " );

      final FileOutputStream fOS = new FileOutputStream( path );
      fOS.write( pdf );
      fOS.close();

      final int wrote = (int) file.length();

      if ( wrote != size ) throw new IOException( "Could not write the entire file!" );

      eC.echo( "Wrote " + size + " bytes! (OK)" );
      return true;
    }
    catch( final Throwable t )
    {
      t.printStackTrace();
      return false;
    }
  }

  public boolean processFile( final String input, final String output )
  {
    final byte[] ps = read( input );
    if ( null == ps ) return false;

    final byte[] pdf = convert( ps );
    if ( null == pdf ) return false;

    final boolean ok = write( output, pdf );
    return ok;
  }
}
