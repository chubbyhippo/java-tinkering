package io.github.chubbyhippo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JschClientTest {

    private final String username = "username";
    private final String password = "password";
    private final String host = "localhost";
    private final int port = 2222;


    @Test
    @DisplayName("should list file from sftp server")
    void shouldListFileFromSftpServer() {
        JschClient jschClient = JschClient.create()
                .withCredentials(username, password, host, port);

    }
}
