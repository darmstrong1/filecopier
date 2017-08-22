package net.sf.fc.gui.v.copy.menu;

import java.awt.event.KeyEvent;

public enum MenuItemData {

    ADD_DST_DIR_PATH(0, "Add destination directory path", "add destination directory path", KeyEvent.VK_A, "Add a destination directory path"),
    ADD_DST_FILE_PATH(0, "Add destination file path", "add destination file path", KeyEvent.VK_A, "Add a destination file path"),
    ADD_SRC_PATH(0, "Add source path", "add source path", KeyEvent.VK_A, "Add a source path"),
    REMOVE_DST_DIR_PATH(0, "Remove destination directory path", "remove destination directory path", KeyEvent.VK_R, "Remove this destination directory path"),
    REMOVE_DST_FILE_PATH(0, "Remove destination file path", "remove destination file path", KeyEvent.VK_R, "Remove this destination file path"),
    REMOVE_SRC_PATH(0, "Remove source path", "remove source path", KeyEvent.VK_R, "Remove this source path"),
    ADD_DST_PATHS(0, "Add destination paths", "add destination paths", KeyEvent.VK_A, "Add group of destination paths. Multiple groups of destination paths should be added if each group will have different copy options."),
    ADD_SRC_PATHS(0, "Add source paths", "add source paths", KeyEvent.VK_A, "Add group of source paths. Multiple groups of source paths should be added if each group will have different copy options."),
    REMOVE_DST_PATHS(1, "Remove destination paths", "remove destination paths", KeyEvent.VK_R, "Remove group of destination paths."),
    REMOVE_SRC_PATHS(1, "Remove source paths", "remove source paths", KeyEvent.VK_R, "Remove group of source paths."),
    ADD_FILE_DSTS(0, "Add file destination paths", "add file destination paths", KeyEvent.VK_A, "Add file destination paths"),
    ADD_DIR_DSTS(0, "Add directory destination paths", "add directory destination paths", KeyEvent.VK_A, "Add directory destination paths"),
    REMOVE_MAP(1, "Remove map", "remove map", KeyEvent.VK_R, "Remove map"),
    ADD_MAP(0, "Add map", "add map", KeyEvent.VK_A, "Add map"),
    ADD_SRC_OPTIONS(2, "Add source options", "add source options", KeyEvent.VK_O, "Add options for source files. Source file options take precedence over default options"),
    ADD_DST_OPTIONS(2, "Add destination options", "add destination options", KeyEvent.VK_O, "Add options for destination files. Destination file options take precedence over source options and default options"),
    REMOVE_SRC_OPTIONS(0, "Remove source options", "remove source options", KeyEvent.VK_R, "Remove source options. The default options will be used"),
    REMOVE_DST_OPTIONS(0, "Remove destination options", "remove destination options", KeyEvent.VK_R, "Remove destination options. The default options will be used or the source options if source options are defined");

    private final int idx;
    private final String text;
    private final String cmd;
    private final int mnemonic;
    private final String toolTip;

    private MenuItemData(int idx, String text, String cmd, int mnemonic, String toolTip) {
        this.idx = idx;
        this.text = text;
        this.cmd = cmd;
        this.mnemonic = mnemonic;
        this.toolTip = toolTip;
    }

    public int getIdx() { return idx; }
    public String getText() { return text; }
    public String getCmd() { return cmd; }
    public int getMnemonic() { return mnemonic; }
    public String getToolTip() { return toolTip; }
}
