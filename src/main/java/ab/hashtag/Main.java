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

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("usage: hashtag <command>\n" +
          //"  copyyaml - copy the tags from ig to yaml\n" +
          "  new - create new tags\n" +
          "  shuffle - shuffle tags\n" +
          "  copy - copy tags from phone\n" +
          "  paste - paste tags to phone");
      System.exit(1);
    }
    switch (args[0]) {
      case "copyyaml": new Service().copySave(); break;
      case "new": new Service().newTags(IntStream.range(1, args.length).mapToObj(a -> args[a])
          .collect(Collectors.toList())); break;
      case "shuffle": new Service().shuffleTags(); break;
      case "copy": new Service().copyPhone(); break;
      case "paste": new Service().pastePhone(); break;
      default: throw new IllegalStateException("unknown command: " + args[0]);
    }
  }

}
