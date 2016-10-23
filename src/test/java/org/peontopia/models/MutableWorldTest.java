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
 * Unit tests for MutableWorld
 */
public class MutableWorldTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  MutableWorld world;
  Map<String, Resource> resources =
      new ResourceLoader().load(ResourceLoaderTest.testResourceData);

  @Before
  public void setUp() {
    world = new MutableWorld(2, 2);
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
  public void testFreeze() {
    assertEquals(false, world.frozen());
    world.freeze();
    assertEquals(true, world.frozen());
  }

  @Test
  public void testAddPeonToFrozen() {
    world.freeze();
    exception.expect(ModifyFrozenException.class);
    world.addPeon(1, 1);
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

  @Test
  public void testCopy() {
    world.addPeon(1, 1);
    world.freeze();
    MutableWorld newWorld = MutableWorld.thaw(world);

    assertEquals(false, newWorld.frozen());
    assertEquals(1, world.tile(1, 1).peons().size());
    assertEquals(1, newWorld.tile(1, 1).peons().size());

    newWorld.addPeon(1, 1);
    assertEquals(1, world.tile(1, 1).peons().size());
    assertEquals(2, newWorld.tile(1, 1).peons().size());
  }

  @Test
  public void testIdConsistentOnCopy() {
    long pid = world.addPeon(1, 1).id();
    long fid = world.addFactory(0, 0, resources.get("Diamond")).id();
    long sid = world.addStore(0, 1).id();

    assertEquals(pid, world.peon(pid).id());
    assertEquals(fid, world.factory(fid).id());
    assertEquals(sid, world.store(sid).id());

    world.freeze();
    assertEquals(pid, world.peon(pid).id());
    assertEquals(fid, world.factory(fid).id());
    assertEquals(sid, world.store(sid).id());

    MutableWorld newWorld = MutableWorld.thaw(world);

    assertEquals(pid, newWorld.peon(pid).id());
    assertEquals(fid, newWorld.factory(fid).id());
    assertEquals(sid, newWorld.store(sid).id());
  }

}
