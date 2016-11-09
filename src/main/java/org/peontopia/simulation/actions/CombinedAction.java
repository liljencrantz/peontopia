package org.peontopia.simulation.actions;

import java.util.Optional;

/**
 * Created by axel on 2016-11-07.
 */
class CombinedAction implements Action {

  private Action first;
  private final Action second;

  CombinedAction(Action first, Action second) {
//    System.err.println("CombinedAction::init");
    this.first = first;
    this.second = second;
  }

  @Override
  public int delay() {
    return first.delay();
  }

  @Override
  public Optional<Action> run() {
  //  System.err.println("CombinedAction::run");
    Optional<Action> res = first.run();
    if (res.isPresent()) {
      first = res.get();
    //  System.err.println("First action wants to run again in " + first.delay() + " turns");
      return Optional.of(this);
    }
    //System.err.println("Run second action");
    return Optional.of(second);
  }

}
