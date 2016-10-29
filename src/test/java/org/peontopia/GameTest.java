package org.peontopia;

import org.junit.Test;
import org.peontopia.models.MutableWorld;
import org.peontopia.simulation.Simulation;

import java.io.IOException;

/**
 * Created by axel on 24/10/16.
 */
public class GameTest {

  @Test
  public void test() throws IOException {
    Simulation s = Game.create(1000, 1000);
    MutableWorld world = s.world();

    for (long i=0; i<20000l; i++) {
      s.step();
    }
    System.err.println("Done all steps");

  }
}
