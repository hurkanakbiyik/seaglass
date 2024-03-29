/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id$
 */
package com.seaglasslookandfeel.painter.button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.ButtonPainter.Which;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * Paint a (possibly) segmented button. The default colors are suitable for
 * drawing on a default background, for instance, a dialog box.
 * 
 * @author Kathryn Huxtable
 */
public class SegmentedButtonPainter extends ButtonVariantPainter {

    enum SegmentStatus {
        NONE, FIRST, MIDDLE, LAST
    };

    private Color            outerFocusColor        = decodeColor("seaGlassOuterFocus");
    private Color            innerFocusColor        = decodeColor("seaGlassFocus");
    private Color            outerToolBarFocusColor = decodeColor("seaGlassToolBarOuterFocus");
    private Color            innerToolBarFocusColor = decodeColor("seaGlassToolBarFocus");

    private Effect           dropShadow             = new SeaGlassDropShadowEffect();

    public ButtonStateColors enabled;
    public ButtonStateColors enabledPressed;
    public ButtonStateColors defaultButton;
    public ButtonStateColors defaultPressed;
    public ButtonStateColors disabled;
    public ButtonStateColors disabledSelected;

    /**
     * Create a segmented button painter.
     * 
     * @param state
     *            the button state.
     * @param ctx
     *            the paint context.
     */
    public SegmentedButtonPainter(Which state, PaintContext ctx) {
        super(state, ctx);

        // Set the default colors.
        setEnabled(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff, true), new Color(
            0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf)));
        setEnabledPressed(new ButtonStateColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDefault(new ButtonStateColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true), new Color(
            0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDefaultPressed(new ButtonStateColors(new Color(0xc0eeeeee, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffB4D9EE, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDisabled(new ButtonStateColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true), new Color(
            0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF)));
        setDisabledSelected(new ButtonStateColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xaaaaaa), new Color(0x8AAFE0), new Color(0x5785BF)));
    }

    public void setEnabled(ButtonStateColors enabled) {
        this.enabled = enabled;
    }

    public void setEnabledPressed(ButtonStateColors enabledPressed) {
        this.enabledPressed = enabledPressed;
    }

    public void setDefault(ButtonStateColors defaultButton) {
        this.defaultButton = defaultButton;
    }

    public void setDefaultPressed(ButtonStateColors defaultPressed) {
        this.defaultPressed = defaultPressed;
    }

    public void setDisabled(ButtonStateColors disabled) {
        this.disabled = disabled;
    }

    public void setDisabledSelected(ButtonStateColors disabledSelected) {
        this.disabledSelected = disabledSelected;
    }

    /**
     * {@inheritDoc}
     */
    public void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {

        ButtonStateColors colors = getButtonColors();
        SegmentStatus segmentStatus = getSegmentStatus(c);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = focusInsets.left;
        int y = focusInsets.top;
        width -= focusInsets.left + focusInsets.right;
        height -= focusInsets.top + focusInsets.bottom;

        boolean useToolBarFocus = isInToolBar(c);
        Shape s;
        if (focused) {
            s = decodeOuterFocus(segmentStatus, x, y, width, height);
            g.setColor(useToolBarFocus ? outerToolBarFocusColor : outerFocusColor);
            g.draw(s);
            s = decodeInnerFocus(segmentStatus, x, y, width, height);
            g.setColor(useToolBarFocus ? innerToolBarFocusColor : innerFocusColor);
            g.draw(s);
        }

        if (!isInToolBar(c) || this instanceof TexturedButtonPainter) {
            s = decodeBorder(segmentStatus, x, y, width, height);
            if (!focused) {
                dropShadow.fill(g, s);
            }
            g.setPaint(decodeGradientBackground(s, colors.backgroundTop, colors.backgroundBottom));
            g.fill(s);
            s = decodeInterior(segmentStatus, x, y, width, height);
            g.setColor(colors.mainColor);
            g.fill(s);
            g.setPaint(decodeGradientBottomShine(s, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
            g.fill(s);
            g.setPaint(decodeGradientTopShine(s, colors.upperShineTop, colors.upperShineBottom));
            g.fill(s);
        }
    }

    /**
     * Get the button colors for the state.
     * 
     * @return the button color set.
     */
    protected ButtonStateColors getButtonColors() {
        switch (state) {
        case BACKGROUND_DEFAULT:
        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            return defaultButton;

        case BACKGROUND_PRESSED_DEFAULT:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            return defaultPressed;

        case BACKGROUND_DISABLED:
            return disabled;

        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            return enabled;

        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return enabledPressed;

        case BACKGROUND_DISABLED_SELECTED:
            return disabledSelected;
        }

        return enabled;
    }

    /**
     * Get the segment status (if any) from the component's client properties.
     * 
     * @param c
     *            the component.
     * @return the segment status.
     */
    protected SegmentStatus getSegmentStatus(JComponent c) {
        Object buttonType = c.getClientProperty("JButton.buttonType");
        SegmentStatus segmentStatus = SegmentStatus.NONE;
        if (buttonType != null && buttonType instanceof String && ((String) buttonType).startsWith("segmented")) {
            String position = (String) c.getClientProperty("JButton.segmentPosition");
            if ("first".equals(position)) {
                segmentStatus = SegmentStatus.FIRST;
            } else if ("middle".equals(position)) {
                segmentStatus = SegmentStatus.MIDDLE;
            } else if ("last".equals(position)) {
                segmentStatus = SegmentStatus.LAST;
            }
        }
        return segmentStatus;
    }

    protected Shape decodeOuterFocus(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) {
        switch (segmentStatus) {
        case FIRST:
            return ShapeUtil.createRoundRectangle(x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUS    }

    protected Shape decodeInterior(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) {
        switch (segmentStatus) {
        cas - 2, w + 3, h + 3);
        case LAST:
            return ShapeUtil.createRoundRectangle(x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUScase MIDDLE:
            return ShapeUtil.createRectangle(x - 2, y, w + 4, h);
        case LAST:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x - 2,x - 2, y - 2, w + 3, h + 3, CornerSize.OUTER_FOCUSyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.ROUNDED, CornerStyle.ROUNDED);
        }
    }

    protected Shape decodeInnerFocus(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) RoundRectangle x - 1, y - 1, w + 2, h + 1, CornerSize.INNER_FOCUS    }

    protected Shape decodeInterior(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) {
        switch (segmentStatus) {
        casrStyle.SQUARE);
        case MIDDLE:
            return ShapeUtil.createRecRoundRectangle(x - 2, y - 1, w + 2, h + 1, CornerSize.INNER_FOCUScase MIDDLE:
            return ShapeUtil.createRectangle(x - 2, y, w + 4, h);
        case LAST:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x - 2,x - 1, y - 1, w + 1, h + 1, CornerSize.INNER_FOCUS  return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x - 1, y - 1, w + 1, h + 1, CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.ROUNDED, CornerStyle.ROUNDED);
        }
    }

    protected Shape decodeBordRoundRectangleix, y, w + 2, h, CornerSize.BORDER    }

    protected Shape decodeInterior(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) {
        switch (segmentStatus) {
        cas, w + 4, h);
        case LAST:
            return ShapeUtil.createRoundRectangle(x - 2, y, w + 2, h, CornerSize.BORDERcase MIDDLE:
            return ShapeUtil.createRectangle(x - 2, y, w + 4, h);
        case LAST:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x - 2,x, y, w, h, CornerSize.BORDERCornerStyle.SQUARE,
                CornerStyle.ROUNDED, CornerStyle.ROUNDED);
        default:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x, y, w, h, CornerStyle.ROUNDED, CornerStyle.ROUNDED, CornerStyle.ROUNDERoundRectangle x + 1, y + 1, w, h - 2, CornerSize.INTERIOR    }

    protected Shape decodeInterior(final SegmentStatus segmentStatus, final int x, final int y, final int w, final int h) {
        switch (segmentStatus) {
        cas + 1, w + 3, h - 2);
        case LAST:
            return ShapeUtil.createRoundRectangle(x - 2, y + 1, w + 1, h - 2, CornerSize.INTERIORcase MIDDLE:
            return ShapeUtil.createRectangle(x - 2, y, w + 4, h);
        case LAST:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x - 2,x + 1, y + 1, w - 2, h - 2, CornerSize.INTERIORd(CornerSize.OUTER_FOCUS, x - 2, y + 1, w + 1, h - 2, CornerStyle.SQUARE, CornerStyle.SQUARE,
                CornerStyle.ROUNDED, CornerStyle.ROUNDED);
        default:
            return ShapeUtil.createQuad(CornerSize.OUTER_FOCUS, x + 1, y + 1, w - 2, h - 2, CornerStyle.ROUNDED, CornerStyle.ROUNDED,
                CornerStyle.ROUNDED, CornerStyle.ROUNDED);
        }
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the button.
     */
    public class ButtonStateColors {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public ButtonStateColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
            float lowerShineMidpoint, Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShineTop = upperShineTop;
            th