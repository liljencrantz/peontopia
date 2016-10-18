package org.peontopia.collections;

import java.util.Iterator;

/**
 * An encapsulation of an iterator that can at different times make the iterator be either mutable or immutable.
 *
 * @param <T>
 */
public class FreezableIterator<T> implements Iterator<T> {

  private Iterator<T> inner;
  private FreezeCheck check;
  public FreezableIterator(Iterator iterator, FreezeCheck check) {
    this.inner = iterator;
    this.check = check;

  }

  @Override
  public boolean hasNext() {
    return inner.hasNext();
  }

  @Override
  public T next() {
    return inner.next();
  }

  @Override
  public void remove() {
    check.check();
    inner.remove();
  }
}
