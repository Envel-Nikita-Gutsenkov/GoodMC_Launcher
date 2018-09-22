package com.skcraft.launcher.windows;

import com.sun.jna.platform.WindowUtils;

import java.awt.*;

public class barOverlay extends Window {
    public barOverlay(Window owner) {
        super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        setBackground(new Color(0,0,0,0));
    }
}
