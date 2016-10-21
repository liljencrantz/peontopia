package org.peontopia.models;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A class that maps each Actor subtype to a different object instance.
 *
 * This is useful because it makes sure that all actor types are covered.
 */
public class ActorMapper<T> {
  
  private final T peon;
  private final T factory;
  private final T store;

  private ActorMapper(T peon, T factory, T store) {
    this.peon = peon;
    this.factory = factory;
    this.store = store;
  }

  public T get(Actor a) {
    if (a instanceof Peon)
      return peon;
    if (a instanceof Factory)
      return factory;
    if (a instanceof Store)
      return store;
    throw new RuntimeException("Unknown actor type " + a.getClass());
  }

  public static <T> Builder<T> builder() {
    return new Builder();
  }
  
  public static class Builder <T>{
    private T peon;
    private T factory;
    private T store;

    public Builder<T> peon(T peon) {
      this.peon = peon;
      return this;
    }

    public Builder<T> factory(T factory) {
      this.factory = factory;
      return this;
    }

    public Builder<T> store(T store) {
      this.store = store;
      return this;
    }

    public ActorMapper<T> build() {
      return new ActorMapper(
          checkNotNull(peon), checkNotNull(factory), checkNotNull(store));
    }
  }

}
