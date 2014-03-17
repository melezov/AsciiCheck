package io;

public enum Terminator
{
  NO( "" ),
  MAC( "\r" ),
  UNIX( "\n" ),
  PC( "\r\n" );

  public final String def;

  private Terminator( final String def )
  {
    this.def = def;
  }

  @Override
  public String toString()
  {
    return def;
  }

//  --------------------------------------------

  public boolean isNewLine( final byte b )
  {
    return ( this != Terminator.NO ) && ( ( this != Terminator.MAC )
        || ( ( b == '\r' ) || ( b != '\n' ) ) );
  }

  public Terminator evolve( final byte b )
  {
    return ( b == '\r' ) ? Terminator.MAC :
        ( b == '\n' ) ? ( this == Terminator.MAC ? Terminator.PC : Terminator.UNIX ) :
          Terminator.NO;
  }
}
