package org.peontopia.simulation.actions;

import org.peontopia.limits.PeonLimits;
import org.peontopia.models.MutableWorld;

import java.util.stream.Collectors;

import static org.peontopia.limits.FactoryLimits.calculateWorkerThroughput;

/**
 * Created by axel on 17/10/16.
 */
public class PeonWork implements Action {

  private final MutableWorld.MutablePeon peon;

  private boolean lookedForJob = false;
  private int workedTicks = 0;

  public PeonWork(MutableWorld.MutablePeon peon) {
    this.peon = peon;
  }


  @Override
  public boolean apply() {
    if (!peon.employer().isPresent() && !lookedForJob) {
      lookedForJob = true;
      findJob();
      return false;
    }

    if (peon.employer().isPresent()) {
      if (workedTicks >= PeonLimits.WORK_TICKS_IN_DAY) {
        work(peon);
        return true;
      }
      workedTicks++;
      return false;
    }

    /* Tried to find a job, but failed. Oh noes! */
    System.err.println("No jerb!!!");
    return true;
  }

  private void work(MutableWorld.MutablePeon peon) {
    MutableWorld.MutableCompany employer = peon.employer().get();
    if (employer instanceof MutableWorld.MutableFactory) {
      MutableWorld.MutableFactory f = (MutableWorld.MutableFactory)employer;
      double throughput = calculateWorkerThroughput(
          f.resource(), f.level()) * PeonLimits.WORK_TICKS_IN_DAY;
      f.addToSupply(f.resource(), throughput);
      f.resource().ingredients().stream()
          .forEach(i -> f.addToSupply(i.resource(), -throughput * i.amount()));
      peon.addMoney(100);
      return;
    }
    throw new RuntimeException("Bad employer type " + employer.getClass());
  }

  private void findJob() {
    peon.world().actors().stream()
        .filter(a -> a instanceof MutableWorld.MutableCompany)
        .map(a -> (MutableWorld.MutableCompany) a)
        .filter(c -> c.employeeOpenings(peon.education()) > 0)
        .findAny()
        .ifPresent(e -> e.addEmployee(peon));
  }
}
