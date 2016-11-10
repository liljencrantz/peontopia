package org.peontopia.simulation;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for PeonSimulator
 */
@RunWith(MockitoJUnitRunner.class)
public class PeonSimulatorTest {

  World world;
  Peon peon;
  PeonSimulator simulator;

  @Mock
  Action.PeonActions actions;

  @Before
  public void setUp() {
    world = new World(5, 5);
    peon = world.addPeon(1, 1);
    simulator = new PeonSimulator(actions);
  }
/*
  @Test
  public void testDiaFromDieFromExhaustion() {
    peon.rest(0);
    Action reply = Action.action(() -> {});
    when(actions.die(peon)).thenReturn(reply);
    assertEquals(simulator.(peon), reply);
  }

  @Test
  public void testThatPeonsDieFromNotStarvation() {
    peon.food(0);
    Action reply = () -> true;
    when(actions.die(peon)).thenReturn(reply);
    assertEquals(simulator.action(peon), reply);
  }

  @Test
  public void testThatWeEatWhenWeAreHungry() {
    peon.food(40);
    Action reply = () -> true;
    when(actions.eat(peon)).thenReturn(reply);
    assertEquals(simulator.action(peon), reply);
  }

  @Test
  public void testThatOurLifeConsistsOfSleepWorkChoresPlayPlayPlayInThatOrder() {
    Action reply1 = () -> true;
    Action reply2 = () -> true;
    Action reply3 = () -> true;
    Action reply4 = () -> true;

    when(actions.sleep(peon)).thenReturn(reply1);
    when(actions.work(peon)).thenReturn(reply2);
    when(actions.chores(peon)).thenReturn(reply3);
    when(actions.play(peon)).thenReturn(reply4);
    assertEquals(simulator.action(peon), reply1);
    assertEquals(simulator.action(peon), reply2);
    assertEquals(simulator.action(peon), reply3);
    assertEquals(simulator.action(peon), reply4);
    assertEquals(simulator.action(peon), reply4);
    assertEquals(simulator.action(peon), reply4);
  }
*/
}
