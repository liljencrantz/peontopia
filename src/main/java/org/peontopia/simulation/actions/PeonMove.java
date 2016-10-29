package org.peontopia.simulation.actions;

import org.peontopia.models.MutableWorld;

/**
 * Created by axel on 16/10/16.
 */
public class PeonMove implements Action {

  private final MutableWorld.MutablePeon peon;
  private final int x;
  private final int y;
  private final boolean relative;

  PeonMove(MutableWorld.MutablePeon peon, int x, int y, boolean relative) {
    this.peon = peon;
    this.x = x;
    this.y = y;
    this.relative = relative;
  }

  @Override
  public boolean apply() {
    if(relative)
      peon.x(peon.x()+x).y(peon.y()+y);
    else
      peon.x(x).y(y);
    return true;
  }
}
