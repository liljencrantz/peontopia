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
 * Unit test for World.Factory
 */
public class FactoryTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  World world;
  Factory factory;
  Map<String, Resource> resources =
      new ResourceLoader().load(ResourceLoaderTest.testResourceData);

  @Before
  public void setUp() {
    world = new World(2, 2);
    factory = world.addFactory(1, 1, resources.get("Diamond"));
  }

  @Test
  public void testBasicCorrectness() {
    assertEquals(resources.get("Diamond"),factory.resource());
  }

  @Test
  public void testMutateFactory() {
    factory.addToSupply(resources.get("Coal"), 3.0);
    assertEquals(3.0, factory.supply(resources.get("Coal")), 0.00001);
  }

  @Test
  public void testCanNotAddUnrelatedResource() {
    exception.expect(RuntimeException.class);
    factory.addToSupply(resources.get("Clay"), 3.0);
  }

}
