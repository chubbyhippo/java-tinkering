package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JSchClientTest {

    private static final int PORT = 22;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final String HOST = "localhost";

    private static SshServer sshServer;

    @TempDir
    private static Path tempDir;

    @BeforeAll
    static void setUp() throws IOException {
        sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(PORT);
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshServer.setPasswordAuthenticator((username, password, session) -> true);
        sshServer.setSubsystemFactories(List.of(new SftpSubsystemFactory()));
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(tempDir));
        sshServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        if (sshServer != null && sshServer.isStarted()) {
            sshServer.stop();
        }
    }

    @Test
    @DisplayName("should list file from sftp server")
    void shouldListFileFromSftpServer() throws JSchException, SftpException, IOException {

        var file = tempDir.resolve("test.txt");
        Files.write(file, "test".getBytes());

        var jschClient = JSchClient.create()
                .withCredentials(USERNAME, PASSWORD, HOST, PORT)
                .withDefaultConfigs()
                .connect();

        var remoteDir = "/";
        var filenames = jschClient.listFiles(remoteDir);

        assertThat(filenames).isNotEmpty();

        Files.delete(file);
    }

}
