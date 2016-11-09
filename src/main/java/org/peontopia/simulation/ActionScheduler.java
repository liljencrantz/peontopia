package org.peontopia.simulation;

import org.peontopia.simulation.actions.Action;

/**
 * Created by axel on 2016-11-05.
 */
public interface ActionScheduler {
  void schedule(Action action);
}
