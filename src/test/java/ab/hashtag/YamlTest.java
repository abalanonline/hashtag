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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlTest {

  @Disabled
  @Test
  void readSnakeyaml() {
    Map<String, List<String>> expected = new Yaml().readSnakeyaml(getClass().getResourceAsStream(Yaml.YAML_FILE));
    Map<String, List<String>> actual = new Yaml().read(getClass().getResourceAsStream(Yaml.YAML_FILE));
    assertEquals(expected, actual);
  }
}
