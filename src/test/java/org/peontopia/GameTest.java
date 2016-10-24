package org.peontopia;

import org.junit.Test;
import org.peontopia.simulation.Simulation;

import java.io.IOException;

/**
 * Created by axel on 24/10/16.
 */
public class GameTest {

  @Test
  public void test() throws IOException {
    Simulation s = Game.create(1000, 1000);
    for (int i=0; i<1000; i++) {
      s.step();
    }
  }
}
