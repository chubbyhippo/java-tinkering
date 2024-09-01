package io.github.chubbyhippo;

public class JschClient {
    private String username;
    private String password;
    private String host;
    private int port;

    private JschClient() {
    }

    public static JschClient create() {
        return new JschClient();
    }

    public JschClient withCredentials(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        return this;
    }
}
