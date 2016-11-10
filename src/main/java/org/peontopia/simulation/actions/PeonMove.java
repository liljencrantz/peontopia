package org.peontopia.simulation.actions;

import org.peontopia.models.Peon;

import static org.peontopia.simulation.actions.Action.action;
import static org.peontopia.simulation.actions.Action.once;

/**
 * Created by axel on 16/10/16.
 */
public class PeonMove {
  static Action move(Peon peon, int x, int y, boolean relative) {
    if (relative) {
      return once(() -> peon.x(peon.x() + x).y(peon.y() + y));
    }
    else {
      return once(() -> peon.x(x).y(y));
    }
  }
}
