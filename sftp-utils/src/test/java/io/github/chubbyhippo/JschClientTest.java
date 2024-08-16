package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JschClientTest {

    private final String username = "foo";
    private final String password = "pass";
    private final String host = "localhost";
    private final int port = 2222;


    @Test
    @DisplayName("should list file from sftp server")
    void shouldListFileFromSftpServer() throws JSchException, SftpException {
        var jschClient = JschClient.create()
                .withCredentials(username, password, host, port)
                .withDefaultConfigs()
                .connect();

        var remoteDir = "/upload";
        var filenames = jschClient.listFiles(remoteDir);

        assertThat(filenames).isNotEmpty();
    }
}
