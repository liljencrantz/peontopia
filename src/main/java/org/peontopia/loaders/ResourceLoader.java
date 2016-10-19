package org.peontopia.loaders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.google.protobuf.util.JsonFormat;

import org.peontopia.models.Resource;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 18/10/16.
 */
public class ResourceLoader {

  public Map<String, Resource> load(String input) {
    return load(new StringReader(input));
  }

  public Map<String, Resource> load(Reader input) {
    Map<String, Resource> res = new HashMap<>();

    Schema.ResourceFile.Builder file = Schema.ResourceFile.newBuilder();

    try {
      JsonFormat.parser().merge(input, file);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Map<String, Schema.Resource> derp = new HashMap<>();
    file.getResourcesList().stream().forEach(r -> derp.put(r.getName(), r));

    if (derp.size() != file.getResourcesList().size()) {
      throw new IllegalArgumentException("Redundant resources in resource file");
    }

    file.getResourcesList().stream().forEach(r -> insertResource(r.getName(), derp, res));
    return ImmutableMap.<String, Resource>builder().putAll(res).build();
  }

  private void insertResource(
      String name,
      Map<String, Schema.Resource> data,
      Map<String, Resource> output) {

    if (output.containsKey(name))
      return;

    if (!data.containsKey(name)) {
      throw new IllegalArgumentException("Unknown resource " + name);
    }

    Schema.Resource template = data.get(name);
    List<Resource.Ingredient> ingredients = new ArrayList<>();

    template.getIngredientsList().stream().forEach(ing -> {
      insertResource(ing.getName(), data, output);
      ingredients.add(new Resource.Ingredient(output.get(ing.getName()), ing.getAmount()));
    });

    output.put(name, new Resource(
        name, template.getAvailability(),
        template.getSpeed(),
        template.getRenewable(),
        template.getClass_(),
        ingredients));
  }
}
