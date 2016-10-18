package org.peontopia.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An encapsulation of a set that can at different times make the set be either mutable or immutable.
 *
 * @param <T>
 */
public class FreezableSet<T> implements Set<T> {

  private final Set<T> inner;
  private final FreezeCheck check;

  public FreezableSet(Set inner, FreezeCheck check) {
    this.inner = inner;
    this.check = check;
  }

  @Override
  public int size() {
    return inner.size();
  }

  @Override
  public boolean isEmpty() {
    return inner.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return inner.contains(o);
  }

  @Override
  public Iterator iterator() {
    return new FreezableIterator(inner.iterator(), check);
  }

  @Override
  public Object[] toArray() {
    return inner.toArray();
  }

  @Override
  public boolean add(T o) {
    check.check();
    return inner.add(o);
  }

  @Override
  public boolean remove(Object o) {
    check.check();
    return inner.remove(o);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    check.check();
    return inner.addAll(c);
  }

  @Override
  public void clear() {
    check.check();
    inner.clear();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    check.check();
    return inner.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    check.check();
    return inner.retainAll(c);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return inner.containsAll(c);
  }

  @Override
  public <T2> T2[] toArray(T2[] a) {
    return inner.toArray(a);
  }

}
