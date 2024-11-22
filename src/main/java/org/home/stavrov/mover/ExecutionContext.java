package org.home.stavrov.mover;

public class ExecutionContext {

    private static String windowToFollowId;
    private static String patternToFollow;

    public static String getWindowToFollowId() {
        return windowToFollowId;
    }

    public static void setWindowToFollowId(String id) {
        windowToFollowId = id;
    }

    public static String getPatternToFollow() {
        return patternToFollow;
    }

    public static void setPatternToFollow(String pattern) {
        patternToFollow = pattern;
    }
}
