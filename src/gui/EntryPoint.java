package gui;
import javax.swing.SwingUtilities;

public class EntryPoint implements Runnable
{
  private final String[] args;

  private EntryPoint( final String[] args )
  {
    this.args = args;
  }

  @Override
  public void run()
  {
    MainFrame.init( args );
  }

  public static void main( final String[] args ) throws Throwable
  {
    SwingUtilities.invokeLater( new EntryPoint( args ) );
  }
}
