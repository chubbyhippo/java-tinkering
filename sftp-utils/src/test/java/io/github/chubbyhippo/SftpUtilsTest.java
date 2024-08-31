package io.github.chubbyhippo;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SftpUtilsTest {

    private static SshServer sshServer;
    private static final int PORT = 22;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("should throw illegal state exception when initialized")
    void shouldThrowIllegalStateExceptionWhenInitialized() {
        var constructor = SftpUtils.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (IllegalStateException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            assertThat(exception.getCause().getClass()).isEqualTo(IllegalStateException.class);
            assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
        }
    }

    @BeforeAll
    public static void setUp() throws IOException {
        sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(PORT);
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshServer.setPasswordAuthenticator((username, password, session) -> true);
        sshServer.setSubsystemFactories(List.of(new SftpSubsystemFactory()));
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory());
        sshServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        if (sshServer != null && sshServer.isStarted()) {
            sshServer.stop();
        }
    }

    @Test
    @DisplayName("should return session when connecting to sftp server")
    void shouldReturnSessionWhenConnectingToSftpServer() throws IOException {
        try (var session = SftpUtils.createSession(USERNAME, "localhost", PORT, PASSWORD)) {
            assertThat(session.isAuthenticated()).isTrue();
            assertThat(session.isClosed()).isTrue();
        }
    }


}