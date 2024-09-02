package io.github.chubbyhippo;

public interface WithCredentials {
    WithDefaultConfigs withCredentials(String username, String password, String host, int port);
}
