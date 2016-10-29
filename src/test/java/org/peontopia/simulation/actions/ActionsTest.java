package org.peontopia.simulation.actions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.peontopia.simulation.actions.Action.compose;
import static org.peontopia.simulation.actions.Action.delay;

/**
 * Unit tests for actions
 */
public class ActionsTest {

  private Action repeatedAction(int times) {
    if (times == 1)
      return () -> true;
    return compose(() -> true, repeatedAction(times - 1));
  }

  private int actionCount(Action a) {
    return a.apply() ? 1 : 1 + actionCount(a);
  }

  @Test
  public void composeTest() {
    assertEquals(1, actionCount(repeatedAction(1)));
    assertEquals(5, actionCount(repeatedAction(5)));
    assertEquals(7, actionCount(compose(repeatedAction(3), repeatedAction(1), repeatedAction(2), repeatedAction(1))));
  }

  @Test
  public void delayTest() {
    assertEquals(3, actionCount(delay(repeatedAction(1), 2)));
    assertEquals(5, actionCount(delay(repeatedAction(2), 3)));
  }

}
