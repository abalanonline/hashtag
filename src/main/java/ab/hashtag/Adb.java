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
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Transfers text to/from mobile device via adb.
 */
public class Adb {

  protected String exec(String command) {
    try {
      Process process = Runtime.getRuntime().exec(command);
      int status = process.waitFor();
      if (status != 0) throw new IOException(new String(process.getErrorStream().readAllBytes()));
      return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  public String getUiXml() {
    String fileName = exec("adb shell uiautomator dump");
    String keyword = "UI hierchary dumped to: ";
    if (!fileName.startsWith(keyword)) throw new IllegalStateException(fileName);
    fileName = fileName.substring(keyword.length());
    String xml = exec("adb shell cat " +  fileName);
    exec("adb shell rm " +  fileName);
    return xml;
  }

  public void click(Point point) {
    exec("adb shell input tap " + point.x + " " + point.y);
  }

  public void type(String s) {
    exec("adb shell input text \"" + s.replace(" ", "%s") + "\"");
  }

}
