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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  protected String getEventType(int eventType) {
    switch (eventType) {
      case XMLStreamConstants.START_ELEMENT: return "START_ELEMENT";
      case XMLStreamConstants.END_ELEMENT: return "END_ELEMENT";
      case XMLStreamConstants.START_DOCUMENT: return "START_DOCUMENT";
      case XMLStreamConstants.END_DOCUMENT: return "END_DOCUMENT";
    }
    throw new IllegalArgumentException("eventType " + eventType);
  }

  protected Map.Entry<String, Map<String, String>> getElement(XMLStreamReader reader) {
    if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) return null;
    Map<String, String> map = new LinkedHashMap<>();
    int count = reader.getAttributeCount();
    for (int i = 0; i < count; i++) map.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
    return Collections.singletonMap(reader.getLocalName(), map).entrySet().iterator().next();
  }

  protected List<String> getIgTags(String xml) {
    try {
      XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(xml));
      while (reader.hasNext()) {
        Map.Entry<String, Map<String, String>> element = getElement(reader);
        reader.next();
        if (element != null && "node".equals(element.getKey())
            && "com.instagram.ui.widget.textview.IgTextLayoutView".equals(element.getValue().get("class"))) {
          Matcher matcher = Pattern.compile("\\W(#\\w+)").matcher(element.getValue().get("text"));
          List<String> list = new ArrayList<>();
          while (matcher.find()) list.add(matcher.group(1));
          return list;
        }
      }
      throw new IllegalStateException("IgTextLayoutView class not found");
    } catch (XMLStreamException e) {
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

  public List<String> copy() {
    return getIgTags(getUiXml());
  }

}
