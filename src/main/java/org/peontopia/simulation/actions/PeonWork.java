package org.peontopia.simulation.actions;

import org.peontopia.limits.PeonLimits;
import org.peontopia.models.MutableCompany;
import org.peontopia.models.MutableFactory;
import org.peontopia.models.Peon;

import java.util.Random;

import static org.peontopia.limits.FactoryLimits.calculateWorkerThroughput;
import static org.peontopia.simulation.actions.Action.noop;
import static org.peontopia.simulation.actions.Action.once;

/**
 * Created by axel on 17/10/16.
 */
public class PeonWork {

  private static final Random random = new Random();
  private final Peon peon;

  private boolean lookedForJob = false;

  public PeonWork(Peon peon) {
    this.peon = peon;
  }

  public static Action create(Peon peon) {
    PeonWork res = new PeonWork(peon);
    return res.apply();
  }

  public Action apply() {
    if (!peon.employer().isPresent() && !lookedForJob) {
    //  System.err.println("Look for jerb!!!!");
      lookedForJob = true;
      findJob();
    }

    if (peon.employer().isPresent()) {
  //    System.err.println("Schedule work work work work work");
      return once(this::work, PeonLimits.WORK_TICKS_IN_DAY+ + random.nextInt(5));
    }

    /* Tried to find a job, but failed. Oh noes! */
    //System.err.println("Dey took err jerbs!!!");
    return noop();
  }

  private void work() {
    MutableCompany employer = peon.employer().get();
    if (employer instanceof MutableFactory) {
      MutableFactory f = (MutableFactory)employer;
      double throughput = calculateWorkerThroughput(
          f.resource(), f.level()) * PeonLimits.WORK_TICKS_IN_DAY;
      f.addToSupply(f.resource(), throughput);
      f.resource().ingredients().stream()
          .forEach(i -> f.addToSupply(i.resource(), -throughput * i.amount()));
      peon.addMoney(100);
//      System.err.println("Performed work!!!");
      return;
    }
    throw new RuntimeException("Bad employer type " + employer.getClass());
  }

  private void findJob() {
    peon.world().actors().stream()
        .filter(a -> a instanceof MutableCompany)
        .map(a -> (MutableCompany) a)
        .filter(c -> c.employeeOpenings(peon.education()) > 0)
        .findAny()
        .ifPresent(e -> e.addEmployee(peon));
  }
}
