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

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Service {

  public static final Path TXT_FILE = Paths.get("hashtag.txt");

  protected ThreadLocalRandom random = ThreadLocalRandom.current();
  protected Adb adb = new Adb();
  protected Ig ig = new Ig();

  protected void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ignore) {}
  }

  protected void humanClick(Rectangle button) {
    int x = button.x + button.width / 4 + random.nextInt(button.width / 2);
    int y = button.y + button.height / 4 + random.nextInt(button.height / 2);
    adb.click(new Point(x, y));
  }

  public List<String> copy() {
    String xml = adb.getUiXml();
    Rectangle more = ig.isCollapsed(xml);
    if (more != null) {
      humanClick(more);
      xml = adb.getUiXml();
    }
    return ig.getIgTags(xml);
  }

  protected List<String> loadTags() {
    try {
      return Files.readAllLines(TXT_FILE);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected void saveTags(List<String> list) {
    try {
      Files.writeString(TXT_FILE, list.stream().map(a -> a + "\n").collect(Collectors.joining()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected void printTags(List<String> list) {
    int length = 0;
    StringBuilder s = new StringBuilder();
    for (String item : list) {
      if (length + item.length() + 3 > 72) {
        s.append("\n");
        length = 0;
      }
      if (length > 0) {
        s.append(" ");
        length++;
      }
      s.append("#" + item);
      length += item.length() + 1;
    }
    System.out.println(s.toString());
  }

  public void copySave() {
    Yaml yaml = new Yaml();
    Set<String> tags = new LinkedHashSet<>(copy());
    yaml.tags().forEach((group, list) -> {
      tags.remove(group);
      tags.removeAll(list);
    });
    System.out.println("copy: new tags " + tags.size());
    yaml.tags(new ArrayList<>(tags));
  }

  public void newTags(List<String> tags) {
    Map<String, Set<String>> yamlTags = new HashMap<>();
    for (Map.Entry<String, List<String>> entry : new Yaml().tags().entrySet()) {
      yamlTags.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }

    Set<String> set = new HashSet<>();
    for (String tag : tags) {
      set.add(tag);
      Optional.ofNullable(new Yaml().tags().get(tag)).ifPresent(set::addAll);
      yamlTags.values().stream().filter(a -> a.contains(tag)).forEach(set::addAll);
    }
    List<String> list = new ArrayList<>(set);
    Collections.shuffle(list);
    saveTags(list);
    System.out.println("new: " + list.size());
    printTags(list);
  }

  public void shuffleTags() {
    List<String> list = loadTags();
    Collections.shuffle(list);
    saveTags(list);
    System.out.println("shuffle: " + list.size());
    printTags(list);
  }

}
