package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JschClientTest {

    private final String username = "username";
    private final String password = "password";
    private final String host = "localhost";
    private final int port = 2222;


    @Test
    @DisplayName("should list file from sftp server")
    void shouldListFileFromSftpServer() throws JSchException, SftpException {
        var jschClient = JschClient.create()
                .withCredentials(username, password, host, port)
                .withDefaultConfigs()
                .connect();

        var remoteDir = "/test";
        var filenames = jschClient.listFiles(remoteDir);

        assertThat(filenames).isNotEmpty();
    }
}
