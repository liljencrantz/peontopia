package org.peontopia.models;

/**
 * This is not some stupid Java builder pattern, this is a simulation of an honest to goodness
 * building that manfactures goods of some sort
 */
public interface Factory extends Company {
  int x();
  int y();
  Resource resource();
  double supply(Resource r);
}
