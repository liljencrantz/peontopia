package org.peontopia;

import org.junit.Test;
import org.peontopia.models.MutableFactory;
import org.peontopia.models.Peon;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.World;

import java.io.IOException;

import static java.lang.String.format;

/**
 * Created by axel on 24/10/16.
 */
public class GameTest {

  @Test
  public void test() throws IOException {
    Game s = Game.create(2000, 2000);
    MutableWorld world = s.world();

    int peon = 0;
    int factory = 0;


    for (long i=0; i< World.TICKS_IN_DAY*40; i++) {
      for (int j = 0; j< 100; j++) {
        if (peon >= 1)
          break;
        Peon pp = world.addPeon(peon / 1900, peon % 1900 + 20);
        s.peonSimulator().simulate(pp, s);
        peon++;

      }

      for (int j = 0; j< 10; j++) {
        if (factory >= 1)
          break;
        MutableFactory ff = world.addFactory(factory / 10, factory % 10, s.resources().get("Glass"));
        s.factorySimulator().simulate(ff, s);
        factory++;
      }

      s.step();
      //System.err.println(format("Step %d, population %d", i, peon));
    }
    System.err.println("Done all steps");
  }
}
