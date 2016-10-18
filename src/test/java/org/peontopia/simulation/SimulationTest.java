package org.peontopia.simulation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.peontopia.simulation.actions.Action.PeonActions.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by axel on 16/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimulationTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Mock
  PeonSimulator peonSimulator;
  Simulation simulation;
  MutableWorld world;
  MutableWorld.MutablePeon peon;

  @Before
  public void setUp() {
    world = new MutableWorld(5, 5);
    peon = world.addPeon(1, 1);
    simulation = new Simulation(world, peonSimulator);
  }

  @Test
  public void testMovePeonAbsolute() {
    when(peonSimulator.step(world, peon)).thenReturn(setCoord(peon, 2, 2));
    World newWorld = simulation.step();
    assertEquals(1, newWorld.tile(2, 2).peons().size());
  }

  @Test
  public void testMovePeonRelative() {
    when(peonSimulator.step(world, peon)).thenReturn(move(peon, -1, 1));
    World newWorld = simulation.step();
    assertEquals(1, newWorld.tile(0, 2).peons().size());
  }

  @Test
  public void testMultiStepActionsTakeCorrectNumberOfTurnsToExecute() {
    when(peonSimulator.step(any(World.class), any(Peon.class)))
        .thenAnswer(i -> Action.compose(w -> true, w -> true, w -> true));
    for (int i=0; i < 12; i++) {
      simulation.step();
    }
    verify(peonSimulator, times(4)).step(any(World.class), any(Peon.class));
  }

  @Test
  public void testPassageOfTime() {
    when(peonSimulator.step(any(World.class), any(Peon.class)))
        .thenAnswer(i -> Action.compose(w -> true));
    World w = null;
    for (int i=0; i < 12; i++) {
      w = simulation.step();
    }
    assertEquals(12, w.time());
  }

  @Test
  public void testMultipleSimulationSteps() {
    World w = world;
    when(peonSimulator.step(any(World.class), any(Peon.class)))
        .thenAnswer(i -> move(((World)i.getArguments()[0]).peon(peon.id()), 0, 1));
    for (int i=0; i < 3; i++) {
      w = simulation.step();
    }
    assertEquals(1, w.tile(1, 4).peons().size());
  }

}
