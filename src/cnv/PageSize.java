package cnv;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum PageSize
{
  A4( "-a4,-a", 210, 297 ),
  A5( "-a5,-p", 140, 210 ),
  A5S( "-a5s,-m", 120, 195 ),
  B4( "-b4,-x,-r,-xb4", 240, 350 ),
  B5( "-b5,-t,-b,-proofs", 170, 240 ),
  B5S( "-b5s,-e,-abs,-pre", 150, 225 );

  private final Set<String> switches;

  private PageSize( final String switches, final int width, final int height )
  {
    this.switches = new LinkedHashSet<String>();

    for ( final String cur : switches.split( "," ) )
      this.switches.add( cur );
  }

  private final Map<String,PageSize> getMap()
  {
    final Map<String,PageSize> swMap = new HashMap<String,PageSize>();

    for ( final PageSize p : values() )
      for ( final String sw : p.switches )
        swMap.put( sw, p );

    return swMap;
  }
}
