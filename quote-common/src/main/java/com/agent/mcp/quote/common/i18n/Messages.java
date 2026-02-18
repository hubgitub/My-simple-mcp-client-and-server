package com.agent.mcp.quote.common.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Messages {

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("common-messages", Locale.getDefault());

    private Messages() {
    }

    public static String get(String key) {
        return BUNDLE.getString(key);
    }

    public static String format(String key, Object... args) {
        return MessageFormat.format(BUNDLE.getString(key), args);
    }
}
