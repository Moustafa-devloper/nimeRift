package com.animrift.app.network;

import java.net.URI;

public final class SourceValidator {

    private SourceValidator() {
    }

    public static boolean isValidHttpUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();

            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    && host != null && !host.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
