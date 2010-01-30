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
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ColorUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ColorUtil.ButtonType;
import com.seaglasslookandfeel.painter.util.ColorUtil.FocusType;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * SpinnerPreviousButtonPainter implementation.
 */
public final class SpinnerPreviousButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        FOREGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_FOCUSED,
        FOREGROUND_PRESSED_FOCUSED,
        FOREGROUND_PRESSED,
    }

    private PaintContext ctx;
    private ButtonType   type;
    private boolean      focused;
    private boolean      isForeground;

    public SpinnerPreviousButtonPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getButtonType(state);
        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED);
        isForeground = isForeground(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (isForeground) {
            paintArrow(g, width, height);
        } else {
            paintButton(g, c, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private ButtonType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case FOREGROUND_DISABLED:
            return ButtonType.DISABLED;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
            return ButtonType.ENABLED;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return ButtonType.PRESSED;
        }
        return null;
    }

    private boolean isForeground(Which state) {
        switch (state) {
        case FOREGROUND_DISABLED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return true;
        default:
            return false;
        }
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;

        if (focused) {
            s = createButtonShape(0, 0, width, height, CornerSize.OUTER_FOCUS);
            ColorUtil.fillFocus(g, s, FocusType.OUTER_FOCUS, useToolBarColors);

            s = createButtonShape(0, 0, width - 1, height - 1, CornerSize.INNER_FOCUS);
            ColorUtil.fillFocus(g, s, FocusType.INNER_FOCUS, useToolBarColors);
        }

        s = createButtonShape(0, 0, width - 2, height - 2, CornerSize.BORDER);
        ColorUtil.fillSpinnerPrevBorderColors(g, s, type);

        s = createButtonShape(1, 1, width - 4, height - 4, CornerSize.INTERIOR);
        ColorUtil.fillSpinnerPrevInteriorColors(g, s, type);

        s = ShapeUtil.createRectangle(1, 0, width - 4, 1);
        ColorUtil.fillSpinnerPrevTopLineColors(g, s, type);
    }

    private void paintArrow(Graphics2D g, int width, int height) {
        Shape s = createArrowShape(width, height);
        ColorUtil.fillSpinnerArrowColors(g, s, type);
    }

    private Shape createButtonShape(int x, int y, int width, int height, CornerSize size) {
        return ShapeUtil.createRoundRectangle(x, y, width, height, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.ROUNDED,
            CornerStyle.SQUARE);
    }

    private Shape createArrowShape(int left, int height) {
        int centerX = 8;
        int centerY = height / 2;
        return ShapeUtil.createArrowDown(centerX, centerY - 2, 4, 3);
    }
}
