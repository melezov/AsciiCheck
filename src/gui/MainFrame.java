package gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
  public static final String TITLE = "ASCII Encoding Checker v1.0 & PostScript 2 PDF v0.2";

  final MainPanel mP;

  public MainFrame()
  {
    super( TITLE );
    setDefaultCloseOperation( DISPOSE_ON_CLOSE );

    mP = new MainPanel();
    getContentPane().add( mP );

    final URL imgPath = MainFrame.class.getClassLoader().getResource( "res/Icon.png" );
    setIconImage( Toolkit.getDefaultToolkit().getImage( imgPath ) );

    setAlwaysOnTop( true );
  }

  public static void setDecoratedLookAndFeel( final boolean lf )
  {
    Toolkit.getDefaultToolkit().setDynamicLayout( true );
    setDefaultLookAndFeelDecorated( lf );
  }

  public void centerOnScreen()
  {
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    final int x4 = screenSize.width >> 2;
    final int y4 = screenSize.height >> 2;

    setBounds( x4, y4, x4 << 1, y4 << 1 );
    setMinimumSize( new Dimension( x4, y4 ) );
    setVisible( true );
  }

  public static void init( final String[] args )
  {
    setDecoratedLookAndFeel( true );
    final MainFrame mF = new MainFrame();

    mF.centerOnScreen();

    for ( final String file : args )
      mF.mP.enqueue( file );
  }

  private static final long serialVersionUID = "AsciiCheck:gui.MainFrame".hashCode();
}
