package org.peontopia.models;

/**
 * Created by axel on 2016-11-10.
 */
class MutableStore extends MutableCompany implements Store {
/*
  public MutableStore(MutableWorld world, Store b) {
    super(world, b);
  }
*/
  public MutableStore(MutableWorld world, int x, int y) {
    super(world, x, y);
  }

  @Override
  public int employeeOpenings(Education education) {
    return 0;
  }

}
