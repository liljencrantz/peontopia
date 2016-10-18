package org.peontopia.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by axel on 15/10/16.
 */
public class FreezableSetTest {
  Set<Integer> inner;
  FreezableSet<Integer> outer;
  boolean frozen;

  @Before
  public void setUp() {
    inner = new HashSet();

    inner.add(1);
    inner.add(2);
    inner.add(3);

    outer = new FreezableSet(inner, () -> {if (frozen) throw new ModifyFrozenException();});
  }

  @Test
  public void testCanIterate() {
    int sum = 0;
    for (int n : outer) {
      sum += n;
    }
    assertEquals(sum, 6);
  }

  @Test
  public void testCanRemove() {
    outer.remove(2);
    assertEquals(inner.size(), 2);
  }

  @Test
  public void testCantRemoveFrozen() {
    frozen = true;
    try {
      outer.remove(2);
    }
    catch (ModifyFrozenException e) {
      assertEquals(outer.size(), 3);
      return;
    }
    assert(false);
  }

}
