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
package com.seaglasslookandfeel.painter;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shapeort java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ComboBoColorUtilainter.PaintContext.CacheMode;

/**
 * ComboBoxPainter implementation.
 */
public final class ComboBoColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ColorUtil.FocusTypeimplementation.
 */
public final class ComboBoxPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_DISABLED_PRESSED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        BACKGROUND_ENABLED_SELECTED,
        BACKGROUND_DISABLED_EDITABLE,
        BACKGROUND_ENABLED_EDITABLE,
        BACKGROUND_FOCUSED_EDITABLE,
        BACKGROUND_PRESSED_EDITABLE,
    }

    private Color                      outerFocusColor        = decodeColor("seaGlassOuterFocus");
    private Color                      innerFocusColor        = dublic ButtonType                  type

    private Path2D                     path                   = new Path2D.Double();

    private Which                      state;
    private Patext               ctx;
    private boolean                    editable;

    public ComboBoxPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        editable = false;
        if (state == Which.BACKGROUND_DISABLED_EDITABLE || state == Which.BACKGROUND_ENABLED_EDITABLE
                || state == Which.BACKGROUND_PRESSED_EDITABLE) {
            editable = true;
        } else if (state == Which.BACKGROUND_FOCUSED_EDITABLE) {
            editable = true;
        } else {
            ComboBoxArrowButtonPainter.Which arrowState;
            if (state == Which.BACKGROUND_DISABLED || state == Which.BACKGROUND_DISABLED_PRESSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_DISABLED_EDITABLE;
            } else if (state == Which.BACKGROUND_PRESSED || state == Which.BACKGROUND_PRESSED_FOCUSED) {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_PRESSED_EDITABLE;
            } else {
                arrowState = ComboBoxArrowButtonPainter.Which.BACKGROUND_ENABLED_EDITABLE;
            }
            buttonPainter = new ComboBoxArrowButtonPainter(arrowState);
        }

        // Set the default colors.
        setEnabled(new ButtonStateColors(new Ctype = getButtonType(state)t c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case BACKGROUND_DISABLED_PRESSED:
            paintDropShadow(g, width, height, true);
            paintDisabled(g, c, width, height);
            break;
        case BACKGROUNDButton:
            paintDropShadow(g, width, height, true);
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_Button:
            paintFocus(g, c, width, height);
            paintEnabled(g, c, width, height);
            break;
        case BACKGROUND_Button_FOCUSED:
            paintFocus(g, c, width, height);
            paintPressed(g, c, width, height);
            break;
        case BACKGROUND_Button:
        case BACKGROUND_ENABLED_SELECTED:
            paintDropShadow(g, width, height, true);
            paintPressed(g, c, width, height);
            break;
        case BACKGROUND_Button_EDITABLE:
            paintFocus(g, c, width, height);
            break;
        case BACKGROUND_DISABLED_EDITABLE:
        case BACKGROUND_ENABLED_EDITABLE:
        case BACKGROUND_PRESSED_EDITABLE:
            paintDropShadow(g, width, height, false);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintDisabled(Graphics2D g, JComponent c, int width, int height) {
 ButtonType getButtonType(Which statese BACKGROUND_DISABLED_PRESSED:
            paintDropShadow(g, width, height, true);
            paintDisabled(g,case BACKGROUND_DISABLED_EDITABLE:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case BACKGROUND_FOCUSED_EDITABLE:
        case BACKGROUND_ENABLED_EDITABLE:
            return ButtonType.ENABLED;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
        case BACKGROUND_ENABLED_SELECTED:
        case BACKGROUND_PRESSED_EDITABLE:
            return ButtonType.PRESSED;
        }
        return null;
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int heightHints.VALUE_ANTIALIAS_ON);

        path = decodeBorder(width, height);
        g.setPaint(decodeGradientBackfinal int leftWidth = width - buttonWidth;

        Shape s = createButtonPath(CornerSize.BORDER, 2, 2, leftWidth - 2, height - 4);
        ColorUtil.fillComboBoxBackgroundBorderColors(g, s, type);

        s = createButtonPath(CornerSize.INTERIOR, 3, 3, leftWidth - 3, height - 6);
        ColorUtil.fillComboBoxBackgroundInteriorColors(g, s, type);

        // Paint arrow button portion.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(leftWidth, 0);
        buttonPainter.doPaint(g2, c, buttonWidth, height, nullJComponent c, int width, int height) {
        g.setColor(isInToolBar(c) ? outerToolBarFocusColor :boolean useToolBarFocus = isInToolBar(cg.fill(path);
       createFocussetColor(isInToolBar(c) ? innerToolBarFocusColor : innerFocg.setPaint(ColorUtil.getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarFocus));
        g.fill(s);
        s = createFocusPath(CornerSize.INNER_FOCUS, 1, 1, width - 2, height - 2);
        g.setPaint(ColorUtil.getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarFocus));
        g.fill(   Shape s = g.getClip();
        if (full) {
            g.setClip(0, 0, width, height);
        } else // FIXME Make this work again.
        // Shape s = g.getClip();
        // if (full) {
        // g.setClip(0, 0, width, height);
        // } else {
        // g.setClip(width - buttonWidth, 0, buttonWidth, height);
        // }
        // g.setColor(outerShadowColor);
        // s = setPath(CornerSize.OUTER_FOCUS, 1, 2, width - 2, height - 2);
        // g.fill(s);
        // g.setColor(innerShadowColor);
        // s = setPath(CornerSize.INNER_FOCUS, 2, 2, width - 4, height - 3);
        // g.fill(s);
        // double y = 2.0;
        width -= 2.0;
 creat 6.0;
        decodeButtonPath(x, y, width, height, arcSize, arcSize);
        return path;
    }

    pRoundRectangletleft, top, width, height, size left, Double top, Double width, Double height, Double arcW, Double arcH) {
        Double bottom = top + height;
        createFocusble right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH)RoundRectangle x, y, width, height, sizettom, left + arcW, bottom);
        path.lineTo(right, bottom);
        p}
