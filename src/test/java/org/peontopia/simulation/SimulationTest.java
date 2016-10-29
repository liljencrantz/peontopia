package org.peontopia.simulation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.peontopia.models.Actor;
import org.peontopia.models.ActorMapper;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Simulator
 */
@RunWith(MockitoJUnitRunner.class)
public class SimulationTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Mock
  PeonSimulator peonSimulator;
  @Mock
  MarketSimulator marketSimulator;
  Simulation simulation;
  MutableWorld world;
  MutableWorld.MutablePeon peon;
  Action.PeonActions actions;

  @Before
  public void setUp() {
    world = new MutableWorld(5, 5);
    peon = world.addPeon(1, 1);
    simulation = new Simulation(
        world,
        ActorMapper.<ActorSimulator>builder()
            .store(a -> () -> true)
            .factory(a -> () -> true)
            .peon(peonSimulator).build() , marketSimulator);
    actions = new Action.PeonActions();
  }

  @Test
  public void testMovePeonAbsolute() {
    when(peonSimulator.step(peon)).thenReturn(actions.setCoord(peon, 2, 2));
    simulation.step();
    assertEquals(1, world.tile(2, 2).peons().size());
  }

  @Test
  public void testMovePeonRelative() {
    when(peonSimulator.step(peon)).thenReturn(actions.move(peon, -1, 1));
    simulation.step();
    assertEquals(1, world.tile(0, 2).peons().size());
  }

  @Test
  public void testMultiStepActionsTakeCorrectNumberOfTurnsToExecute() {
    when(peonSimulator.step(peon))
        .thenAnswer(i -> Action.compose(() -> true, () -> true, () -> true));
    for (int i=0; i < 12; i++) {
      simulation.step();
    }
    verify(peonSimulator, times(4)).step(any(Peon.class));
  }

  @Test
  public void testPassageOfTime() {
    when(peonSimulator.step(peon))
        .thenAnswer(i -> Action.compose(() -> true));
    for (int i=0; i < 12; i++) {
      simulation.step();
    }
    assertEquals(12, world.time());
  }

  @Test
  public void testMultipleSimulationSteps() {
    when(peonSimulator.step(peon))
        .thenAnswer(i -> actions.move(peon, 0, 1));
    for (int i=0; i < 3; i++) {
      simulation.step();
    }
    assertEquals(1, world.tile(1, 4).peons().size());
  }

}
