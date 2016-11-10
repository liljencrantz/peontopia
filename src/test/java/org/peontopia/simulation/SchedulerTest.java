package org.peontopia.simulation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.peontopia.simulation.actions.Action;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertEquals;
import static org.peontopia.simulation.actions.Action.action;
import static org.peontopia.simulation.actions.Action.maybe;

/**
 * Unit tests for Schedule. Also tests the basic actions, because the two are easy to test together
 * and I couldn't be bothered.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {
  @Rule
  public final ExpectedException exception = ExpectedException.none();

  Scheduler scheduler;
  AtomicInteger count;

  @Before
  public void setUp() {
    count = new AtomicInteger();
    scheduler = new Scheduler();
  }

  @Test
  public void testScheduleNow() {
    Action a = Action.once(() -> count.addAndGet(1));
    scheduler.schedule(a);
    assertEquals(0, count.get());
    scheduler.step();
    assertEquals(1, count.get());
    scheduler.step();
    assertEquals(1, count.get());
  }

  @Test
  public void testScheduleLater() {
    Action a = Action.once(() -> count.addAndGet(1), 2);
    scheduler.schedule(a);
    scheduler.step();
    scheduler.step();
    assertEquals(0, count.get());
    scheduler.step();
    assertEquals(1, count.get());
    scheduler.step();
    assertEquals(1, count.get());
  }

  Supplier<Action> recursiveIncrement() {
    count.addAndGet(1);
    return () -> action(recursiveIncrement());
  }

  @Test
  public void testScheduleRepeated() {
    scheduler.schedule(action(() -> action(recursiveIncrement())));
    assertEquals(0, count.get());
    scheduler.step();
    assertEquals(1, count.get());
    scheduler.step();
    assertEquals(2, count.get());
    scheduler.step();
    assertEquals(3, count.get());
  }

  Supplier<Optional<Action>> recursiveOptionalIncrement() {
    count.addAndGet(1);
    return () -> Optional.of(maybe(recursiveOptionalIncrement()));
  }

  @Test
  public void testScheduleRepeatedOptional() {
    scheduler.schedule(action(() -> maybe(recursiveOptionalIncrement())));
    assertEquals(0, count.get());
    scheduler.step();
    assertEquals(1, count.get());
    scheduler.step();
    assertEquals(2, count.get());
    scheduler.step();
    assertEquals(3, count.get());
  }
}
