package org.peontopia.simulation.analysis;

import static java.lang.Math.pow;

/**
 * Created by axel on 20/10/16.
 */
public class SalaryAnalysis {

  private static final double BASE_SALARY = 100;
  private static final double SALARY_EXPONENT = 1.5;


  public double baseDailySalary(double educationLevel) {
    return BASE_SALARY * pow(SALARY_EXPONENT, educationLevel);
  }

}
