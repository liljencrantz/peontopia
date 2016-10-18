package org.peontopia.simulation.actions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.peontopia.simulation.actions.Action.compose;

/**
 * Created by axel on 17/10/16.
 */
public class ActionsTest {

  private Action repeatedAction(int times) {
    if (times == 1)
      return world -> true;
    return compose(world -> true, repeatedAction(times - 1));
  }

  private int actionCount(Action a) {
    return a.apply(null) ? 1 : 1 + actionCount(a);
  }

  @Test
  public void composeActionTest() {
    assertEquals(1, actionCount(repeatedAction(1)));
    assertEquals(5, actionCount(repeatedAction(5)));
    assertEquals(7, actionCount(compose(repeatedAction(3), repeatedAction(1), repeatedAction(2), repeatedAction(1))));
  }

}
