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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Service {

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

  public void copySave() {
    Yaml yaml = new Yaml();
    Set<String> tags = new LinkedHashSet<>(copy());
    yaml.tags().forEach((group, list) -> {
      tags.remove(group);
      tags.removeAll(list);
    });
    System.out.println();
    yaml.tags(new ArrayList<>(tags));
  }

}
