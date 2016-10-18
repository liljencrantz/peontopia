package org.peontopia.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by axel on 15/10/16.
 */
public class FreezableIteratorTest {

  ArrayList<Integer> inner;
  FreezableIterator<Integer> outer;
  boolean frozen;

  @Before public void setUp() {
    inner = new ArrayList();

    inner.add(1);
    inner.add(2);
    inner.add(3);

    outer = new FreezableIterator(inner.iterator(), () -> {if (frozen) throw new ModifyFrozenException();});
  }

  @Test
  public void testCanIterate() {
    int sum = 0;
    for (; outer.hasNext();) {
      sum += outer.next();
    }
    assertEquals(sum, 6);
  }

  @Test
  public void testCanRemove() {
    for (; outer.hasNext();) {
      outer.next();
      outer.remove();
    }
    assertEquals(inner.size(), 0);
  }

  @Test
  public void testCantRemoveFrozen() {
    frozen = true;
    try {
      outer.remove();
    }
    catch (ModifyFrozenException e) {
      assertEquals(inner.size(), 3);
      return;
    }
    assert(false);
  }
}
