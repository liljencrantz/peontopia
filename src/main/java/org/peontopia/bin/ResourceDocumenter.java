package org.peontopia.bin;

import org.peontopia.loaders.ResourceLoader;
import org.peontopia.models.Resource;
import org.peontopia.simulation.analysis.FactoryAnalysis;
import org.peontopia.simulation.analysis.ResourceAnalysis;
import org.peontopia.simulation.analysis.SalaryAnalysis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Write out a simple overview of all resources with aproximate values as HTML.
 */
public class ResourceDocumenter {

  private final FactoryAnalysis factoryAnalysis = new FactoryAnalysis();
  private final SalaryAnalysis salaryAnalysis = new SalaryAnalysis();
  private final ResourceLoader loader = new ResourceLoader();
  private final Map<String, Resource> resources;
  private final ResourceAnalysis resourceAnalysis;

  private ResourceDocumenter() {
    InputStream in = getClass().getResourceAsStream("/resources.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    resources = loader.load(reader);
    resourceAnalysis = new ResourceAnalysis(resources, 3, factoryAnalysis, salaryAnalysis);
  }

  private void print(Object... str) {
    for(Object s : str) {
      System.out.print(s);
    }
    System.out.println();
  }

  public void document() {
    print("var resources = [");
    resources.values().stream().forEach( r -> {
      print(String.format("  {\"name\": \"%s\", \"value\": %.2f}", r.name(), resourceAnalysis.getValue(r)));
    });
    print("];");
  }

  public static void main(String[] args) {
    new ResourceDocumenter().document();
  }

}
