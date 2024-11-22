package org.home.stavrov.utils;

import com.sun.jna.platform.win32.WinDef;

public class WindowInfo {

    private String name;
    private WinDef.HWND id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WinDef.HWND getId() {
        return id;
    }

    public void setId(WinDef.HWND id) {
        this.id = id;
    }
}
