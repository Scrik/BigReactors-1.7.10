package erogenousbeef.bigreactors.core.client.gui.widget;

import java.awt.*;
import java.util.*;

public class GuiToolTip {

    private static final long DELAY = 0;

    protected Rectangle bounds;

    private long mouseOverStart;

    protected final java.util.List<String> text;

    private int lastMouseX = -1;

    private int lastMouseY = -1;

    private boolean visible = true;

    public GuiToolTip(Rectangle bounds, String... lines) {
        this.bounds = bounds;
        if (lines != null) {
            text = new ArrayList<String>(lines.length);
            for (String line : lines) {
                text.add(line);
            }
        } else {
            text = new ArrayList<String>();
        }
    }

    public GuiToolTip(Rectangle bounds, java.util.List<String> lines) {
        this.bounds = bounds;
        if (lines == null) {
            text = new ArrayList<String>();
        } else {
            text = new ArrayList<String>(lines);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void onTick(int mouseX, int mouseY) {
        if (lastMouseX != mouseX || lastMouseY != mouseY) {
            mouseOverStart = 0;
        }

        if (bounds.contains(mouseX, mouseY)) {

            if (mouseOverStart == 0) {
                mouseOverStart = System.currentTimeMillis();
            }
        } else {
            mouseOverStart = 0;
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    public boolean shouldDraw() {
        if (!visible) {
            return false;
        }
        updateText();
        if (mouseOverStart == 0) {
            return false;
        }
        return System.currentTimeMillis() - mouseOverStart >= DELAY;
    }

    protected void updateText() {
    }

    public void setToolTipText(String... txt) {
        text.clear();
        if (txt != null) {
            for (String line : txt) {
                text.add(line);
            }
        }
    }

    public java.util.List<String> getToolTipText() {
        return text;
    }

}
