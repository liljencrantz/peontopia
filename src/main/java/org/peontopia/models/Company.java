package org.peontopia.models;

import java.util.Collection;

/**
 * Created by axel on 18/10/16.
 */
public interface Company extends Building {
  int START_MONEY = 100000;

  int money();
  Collection<Peon> employees();
  int employeeOpenings(Education education);
}
