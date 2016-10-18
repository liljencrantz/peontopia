package org.peontopia.simulation.actions;

import org.peontopia.models.MutableWorld;

/**
 * Created by axel on 16/10/16.
 */
public class PeonMove implements Action {

  private final long id;
  private final int x;
  private final int y;
  private final boolean relative;

  PeonMove(long id, int x, int y, boolean relative) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.relative = relative;
  }

  @Override
  public boolean apply(MutableWorld world) {
    MutableWorld.MutablePeon p = world.peon(id);
    if(relative)
      p.x(p.x()+x).y(p.y()+y);
    else
      p.x(x).y(y);
    return true;
  }
}
