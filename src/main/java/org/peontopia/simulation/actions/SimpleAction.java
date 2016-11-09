package org.peontopia.simulation.actions;

import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by axel on 2016-11-07.
 */
class SimpleAction implements Action{

  private final Supplier<Optional<Action>> code;
  private final int delay;

  SimpleAction(Supplier<Optional<Action>> runnable, int delay) {
    checkArgument(delay >= 0);
    this.code = runnable;
    this.delay = delay;
  }

  @Override
  public int delay() {
    return delay;
  }

  @Override
  public Optional<Action> run() {
    return code.get();
  }

}
