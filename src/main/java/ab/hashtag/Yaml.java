/*
 * Copyright (C) 2025 Aleksei Balan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ab.hashtag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Yaml {

  public static final String YAML_FILE = "hashtag.yaml";

  public Map<String, List<String>> readSnakeyaml(InputStream stream) {
    //return new org.yaml.snakeyaml.Yaml().load(stream);
    throw new IllegalStateException();
  }

  /**
   * This method can read the yaml indented with 2 spaces.
   */
  public Map<String, List<String>> read(InputStream stream) {
    Map<String, List<String>> map = new LinkedHashMap<>();
    List<String> lines = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());
    String group = null;
    for (String line : lines) {
      if (line.trim().isEmpty()) continue;
      int comment = line.indexOf(" #");
      if (comment >= 0) line = line.substring(0, comment);
      if (line.endsWith(":") && line.indexOf(' ') < 0) {
        group = line.substring(0, line.length() - 1);
        continue;
      }
      if (line.startsWith("  - ")) {
        String line4 = line.substring(4);
        if (group != null) {
          map.computeIfAbsent(group, a -> new ArrayList<>()).add(line4.trim());
          continue;
        }
      }
      throw new IllegalStateException("error yaml: " + line);
    }
    return map;
  }

  public Map<String, List<String>> tags() {
    try {
      Path path = Paths.get(YAML_FILE);
      InputStream stream = Files.exists(path) ? Files.newInputStream(path) : getClass().getResourceAsStream(YAML_FILE);
      return read(stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void tags(List<String> tags) {
    if (tags.isEmpty()) return;
    Path path = Paths.get(YAML_FILE);
    String s = Files.exists(path) ? "" : "random:\n";
    s += tags.stream().map(a -> "  - " + a + "\n").collect(Collectors.joining());
    try (OutputStream stream = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
      stream.write(s.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
