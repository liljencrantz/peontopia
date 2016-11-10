package org.peontopia.models;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.peontopia.collections.ModifyFrozenException;

import static org.junit.Assert.assertEquals;

/**
 * Created by axel on 16/10/16.
 */
public class PeonTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  World world;
  Peon peon;

  @Before
  public void setUp() {
    world = new World(2, 2);
    peon = world.addPeon(1, 1);
  }

  @Test
  public void testMutatePeon() {
    peon.food(2);
    assertEquals(2, peon.food());
    peon.addFood(3);
    assertEquals(5, peon.food());

    peon.happiness(2);
    assertEquals(2, peon.happiness(), 0.00001);
    peon.addHappiness(3);
    assertEquals(5, peon.happiness(), 0.00001);

    peon.money(2);
    assertEquals(2, peon.money());
    peon.addMoney(3);
    assertEquals(5, peon.money());

    peon.rest(2);
    assertEquals(2, peon.rest());
    peon.addRest(3);
    assertEquals(5, peon.rest());

    peon.x(0);
    assertEquals(0, peon.x());
    peon.addX(1);
    assertEquals(1, peon.x());

    peon.y(0);
    assertEquals(0, peon.y());
    peon.addY(1);
    assertEquals(1, peon.y());
  }

  @Test
  public void testMove() {
    peon.y(0);
    assertEquals(world.tile(1, 0).peons().size(), 1);
    assertEquals(world.tile(1, 1).peons().size(), 0);
    peon.x(0);
    assertEquals(world.tile(0, 0).peons().size(), 1);
    assertEquals(world.tile(1, 0).peons().size(), 0);
  }

  @Test
  public void testInvalidMove() {
    exception.expect(IndexOutOfBoundsException.class);
    peon.y(5);
  }

  @Test
  public void testInvalidMoveRelative() {
    exception.expect(IndexOutOfBoundsException.class);
    peon.addY(5);
  }

}
