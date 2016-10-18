package org.peontopia.simulation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.peontopia.models.MutableWorld;
import org.peontopia.simulation.actions.Action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by axel on 18/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PeonSimulatorTest {

  MutableWorld world;
  MutableWorld.MutablePeon peon;
  PeonSimulator simulator;

  @Mock
  Action.PeonActions actions;

  @Before
  public void setUp() {
    world = new MutableWorld(5, 5);
    peon = world.addPeon(1, 1);
    simulator = new PeonSimulator(actions);
  }

  @Test
  public void testDieFromNotSleeping() {
    peon.rest(0);
    Action reply = w -> true;
    when(actions.die(peon)).thenReturn(reply);
    assertEquals(simulator.step(world, peon), reply);
  }

  @Test
  public void testDieFromNotEating() {
    peon.food(0);
    Action reply = w -> true;
    when(actions.die(peon)).thenReturn(reply);
    assertEquals(simulator.step(world, peon), reply);
  }

  @Test
  public void testThatWeEatWhenWeAreHungry() {
    peon.food(40);
    Action reply = w -> true;
    when(actions.eat(peon)).thenReturn(reply);
    assertEquals(simulator.step(world, peon), reply);
  }

  @Test
  public void testThatOurLifeConsistsOfSleepWorkChoresPlayPlayPlayInThatOrder() {
    Action reply1 = w -> true;
    Action reply2 = w -> true;
    Action reply3 = w -> true;
    Action reply4 = w -> true;

    when(actions.sleep(peon)).thenReturn(reply1);
    when(actions.work(peon)).thenReturn(reply2);
    when(actions.chores(peon)).thenReturn(reply3);
    when(actions.play(peon)).thenReturn(reply4);
    assertEquals(simulator.step(world, peon), reply1);
    assertEquals(simulator.step(world, peon), reply2);
    assertEquals(simulator.step(world, peon), reply3);
    assertEquals(simulator.step(world, peon), reply4);
    assertEquals(simulator.step(world, peon), reply4);
    assertEquals(simulator.step(world, peon), reply4);
  }

}