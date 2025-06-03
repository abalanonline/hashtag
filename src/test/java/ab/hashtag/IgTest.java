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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IgTest {

  @Test
  void getTags() {
    String empty = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><hierarchy rotation=\"0\"></hierarchy>";
    String collapsed = "<node index=\"3\" text=\"\" resource-id=\"\" class=\"android.widget.LinearLayout\" package=\"com.instagram.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,1490][1440,1568]\"><node index=\"0\" text=\"timhortonsqc Ce temps-là est enfin revenu &#127800;☕☀️… more\" resource-id=\"com.instagram.android:id/row_feed_comment_textview_layout\" class=\"com.instagram.ui.widget.textview.IgTextLayoutView\" package=\"com.instagram.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[49,1490][1440,1554]\"><node index=\"0\" text=\"\" resource-id=\"\" class=\"android.widget.Button\" package=\"com.instagram.android\" content-desc=\"timhortonsqc\" checkable=\"false\" checked=\"false\" clickable=\"true\" enabled=\"true\" focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[49,1490][343,1547]\" /><node index=\"1\" text=\"\" resource-id=\"\" class=\"android.widget.Button\" package=\"com.instagram.android\" content-desc=\"Ce temps-là est enfin revenu &#127800;☕☀️\" checkable=\"false\" checked=\"false\" clickable=\"true\" enabled=\"true\" focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[355,1490][1168,1547]\" /><node index=\"2\" text=\"\" resource-id=\"\" class=\"android.widget.Button\" package=\"com.instagram.android\" content-desc=\"Ce temps-là est enfin revenu &#127800;☕☀️\" checkable=\"false\" checked=\"false\" clickable=\"true\" enabled=\"true\" focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[355,1490][1168,1547]\" /><node index=\"3\" text=\"\" resource-id=\"\" class=\"android.widget.Button\" package=\"com.instagram.android\" content-desc=\"more\" checkable=\"false\" checked=\"false\" clickable=\"true\" enabled=\"true\" focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[1213,1490][1327,1547]\" /></node></node>";
    String expanded = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><node index=\"0\" " +
        "text=\"timhortonsqc Ce temps-là est enfin revenu &#127800;☕☀️&#10;&#10;#TimHortons\" " +
        "resource-id=\"com.instagram.android:id/row_feed_comment_textview_layout\" " +
        "class=\"com.instagram.ui.widget.textview.IgTextLayoutView\" package=\"com.instagram.android\" " +
        "content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" " +
        "focusable=\"true\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" " +
        "selected=\"false\" bounds=\"[49,808][1440,1000]\"></node>";
    String test12 = "<node text=\"name #tag1 #tag2\" class=\"com.instagram.ui.widget.textview.IgTextLayoutView\" />";

    Ig ig = new Ig();
    assertThrows(RuntimeException.class, () -> ig.getIgTags(empty));
    assertEquals(Collections.emptyList(), ig.getIgTags(collapsed));
    assertEquals(Collections.singletonList("TimHortons"), ig.getIgTags(expanded));
    assertEquals(Arrays.asList("tag1", "tag2"), ig.getIgTags(test12));

    assertNotNull(ig.isCollapsed(collapsed));
    assertNull(ig.isCollapsed(expanded));
  }
}
