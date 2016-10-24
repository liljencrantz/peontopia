package org.peontopia.simulation.actions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A Stream collector that converts a stream of actions into a single action. Throws a runtime
 * exceptino if there are no actions to take.
 */
public class ActionCollector implements Collector<Action, List<Action>, Action> {

  @Override
  public Supplier<List<Action>> supplier() {
    return () -> Lists.newArrayList();
  }

  @Override
  public BiConsumer<List<Action>, Action> accumulator() {
    return (builder, action) -> builder.add(action);
  }

  @Override
  public BinaryOperator<List<Action>> combiner() {
    return (l1, l2) -> {l1.addAll(l2); return l1;};
  }

  @Override
  public Function<List<Action>, Action> finisher() {
    return list -> Action.compose(list);
  }

  @Override
  public Set<Characteristics> characteristics() {
    return ImmutableSet.of();
  }
}
