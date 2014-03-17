package io;

import java.util.LinkedHashMap;
import java.util.Map;

public class Line
{
  private static final boolean[] ALLOWED_CHARS;
  static
  {
    ALLOWED_CHARS = new boolean[ 1 << Byte.SIZE ];

    final String allowedChars =  "\t\n\r !\"#$%&'()*+,-./0123456789:;<=>?@" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    for ( final char c : allowedChars.toCharArray() )
      ALLOWED_CHARS[ c ] = true;

  //  ################################################################################
  }

  public final byte[] bytes;
  public final Terminator ending;
  public final Map<Integer,Byte> errMap;

  public Line( final byte[] bytes, final Terminator ending )
  {
    this.bytes = bytes;
    this.ending = ending;

    errMap = new LinkedHashMap<Integer, Byte>();

    for ( int i = 0; i < bytes.length; i ++ )
    {
      final byte b = bytes[ i ];

      if ( !ALLOWED_CHARS[ b & 0xff ] )
      {
        errMap.put( i, b );
        bytes[ i ] = '?';
      }
    }
  }
}
