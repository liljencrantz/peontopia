package org.peontopia.loaders;

import org.junit.Test;
import org.peontopia.models.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ResourceLoaderTest {

  ResourceLoader loader = new ResourceLoader();

  public static final String testResourceData = "{\"resources\":[" +
      "{\"name\":\"Coal\",\"speed\":5,\"availability\":4,\"renewable\":true,\"class\":\"food\"},"+
      "{\"name\":\"Diamond\",\"speed\":1,\"ingredients\":[{\"name\":\"Coal\",\"amount\":1000}]},"+
      "{\"name\":\"Clay\"}"+
      "]}";

  @Test
  public void testSimpleString() {
    Map<String, Resource> res = loader.load(testResourceData);

    assertEquals(res.size(), 3);

    assertEquals(res.get("Coal").speed(), 5);
    assertEquals(res.get("Coal").availability(), 4);
    assertEquals(res.get("Coal").renewable(), true);
    assertEquals(res.get("Coal").resourceClass(), "food");

    assertEquals(res.get("Diamond").ingredients().size(), 1);
    assertEquals(res.get("Diamond").ingredients().iterator().next().resource().name(), "Coal");
    assertEquals(res.get("Diamond").ingredients().iterator().next().amount(), 1000, 0.001);
  }

  @Test
  public void testActualResourceFile() {
    InputStream in = getClass().getResourceAsStream("/resources.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    assert(loader.load(reader).size()>10);
  }
}
