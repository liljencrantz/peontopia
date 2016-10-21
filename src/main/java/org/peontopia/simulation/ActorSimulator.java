package org.peontopia.simulation;

import org.peontopia.models.Actor;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

/**
 * Created by axel on 21/10/16.
 */
public interface ActorSimulator {
  Action step(World w, Actor a);
}
