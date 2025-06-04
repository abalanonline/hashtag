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
import java.awt.Point;
import java.awt.Rectangle;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ig {

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

  protected List<Map.Entry<String, Map<String, String>>> getElements(String xml) {
    ArrayList<Map.Entry<String, Map<String, String>>> list = new ArrayList<>();
    try {
      XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(xml));
      while (reader.hasNext()) {
        list.add(getElement(reader));
        reader.next();
      }
      return list;
    } catch (XMLStreamException e) {
      throw new IllegalStateException(e);
    }
  }

  protected boolean isIgTextLayoutView(Map.Entry<String, Map<String, String>> element) {
    return element != null && "node".equals(element.getKey())
          && "com.instagram.ui.widget.textview.IgTextLayoutView".equals(element.getValue().get("class"));
  }

  protected void validateSingleIgTextLayoutView(List<Map.Entry<String, Map<String, String>>> elements) {
    long count = elements.stream().filter(this::isIgTextLayoutView).count();
    if (count < 1) throw new IllegalStateException("IgTextLayoutView class not found");
    if (count > 1) throw new IllegalStateException("IgTextLayoutView multiple classes found");
  }

  protected Rectangle getBounds(String bounds) {
    Pattern pattern = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]");
    Matcher m = pattern.matcher(bounds);
    if (!m.matches()) throw new IllegalStateException("bounds " + bounds);
    Rectangle rectangle = new Rectangle(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
    rectangle.add(new Point(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
    return rectangle;
  }

  public List<String> getIgTags(String xml) {
    List<Map.Entry<String, Map<String, String>>> elements = getElements(xml);
    validateSingleIgTextLayoutView(elements);
    for (Map.Entry<String, Map<String, String>> element : elements) {
      if (isIgTextLayoutView(element)) {
        Matcher matcher = Pattern.compile("\\W#([^#\\s]+)").matcher(element.getValue().get("text"));
        List<String> list = new ArrayList<>();
        while (matcher.find()) list.add(matcher.group(1));
        return list;
      }
    }
    throw new IllegalStateException();
  }

  public Rectangle isCollapsed(String xml) {
    List<Map.Entry<String, Map<String, String>>> elements = getElements(xml);
    validateSingleIgTextLayoutView(elements);
    Rectangle textRectangle = null;
    for (Map.Entry<String, Map<String, String>> element : elements) {
      if (isIgTextLayoutView(element)) textRectangle = getBounds(element.getValue().get("bounds"));
      if (element != null
          && "node".equals(element.getKey())
          && "android.widget.Button".equals(element.getValue().get("class"))
          && "more".equals(element.getValue().get("content-desc"))
          && textRectangle != null
          && textRectangle.contains(getBounds(element.getValue().get("bounds")))
      ) {
        return getBounds(element.getValue().get("bounds"));
      }
    }
    return null;
  }

}
