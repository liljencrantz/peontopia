package org.peontopia.models;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.peontopia.collections.ModifyFrozenException;
import org.peontopia.loaders.ResourceLoader;
import org.peontopia.loaders.ResourceLoaderTest;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for World
 */
public class WorldTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  World world;
  Map<String, Resource> resources =
      new ResourceLoader().load(ResourceLoaderTest.testResourceData);

  @Before
  public void setUp() {
    world = new World(2, 2);
  }

  @Test
  public void testAddPeon() {
    world.addPeon(1, 1);
    assertEquals(1, world.actors().size());
    assertEquals(1, world.tile(1,1).peons().size());
  }

  @Test
  public void testAddFactory() {
    world.addFactory(1,1,resources.get("Diamond"));
    assertEquals(1, world.actors().size());
  }

  @Test
  public void testAddStore() {
    world.addStore(1, 1);
    assertEquals(1, world.actors().size());
  }

  @Test
  public void testAddPeonToBadLocation() {
    exception.expect(IndexOutOfBoundsException.class);
    world.addPeon(3, 0);
  }

  @Test
  public void testAddBuildingToBadLocation() {
    exception.expect(IndexOutOfBoundsException.class);
    world.addFactory(3, 0, resources.get("Diamond"));
  }

  @Test
  public void testGetBadTile() {
    exception.expect(IndexOutOfBoundsException.class);
    world.tile(3, 0);
  }

}
