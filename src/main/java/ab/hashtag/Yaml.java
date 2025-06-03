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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Yaml {

  public static final String YAML_FILE = "hashtag.yaml";

  public Map<String, List<String>> read(InputStream stream) {
    return new org.yaml.snakeyaml.Yaml().load(stream);
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

}
