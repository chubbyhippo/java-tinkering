package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class JSchClientTest {

    private static final int PORT = 22;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HOST = "localhost";

    private SshServer sshServer;

    @TempDir
    private static Path localDirPath;
    @TempDir
    private static Path remoteDirPath;

    @BeforeEach
    void setUp() throws IOException {
        sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(PORT);
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshServer.setPasswordAuthenticator((username, password, session) -> true);
        sshServer.setSubsystemFactories(List.of(new SftpSubsystemFactory()));
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(remoteDirPath));
        sshServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (sshServer != null && sshServer.isStarted()) {
            sshServer.stop();
        }
    }

    @Test
    @DisplayName("should list file from sftp server")
    void shouldListFileFromSftpServer() throws JSchException, SftpException, IOException {

        var file = remoteDirPath.resolve("test.txt");
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

    @Test
    @DisplayName("should download file")
    void shouldDownloadFile() throws IOException, JSchException, SftpException {

        var file = remoteDirPath.resolve("test.txt");
        Files.write(file, "test".getBytes());

        var jSchClient = JSchClient.create()
                .withCredentials(USERNAME, PASSWORD, HOST, PORT)
                .withDefaultConfigs()
                .connect();

        var remoteDir = "/test.txt";

        var inputStream = jSchClient.downloadFile(remoteDir);
        assertThat(inputStream).isNotNull();

        Files.delete(file);
    }

    @Test
    @DisplayName("should upload file")
    void shouldUploadFile() throws JSchException, IOException, SftpException {

        var jSchClient = JSchClient.create()
                .withCredentials(USERNAME, PASSWORD, HOST, PORT)
                .withDefaultConfigs()
                .connect();

        var localFilePath = localDirPath.resolve("test.txt");

        Files.write(localFilePath, "test".getBytes());

        var localPath = localFilePath.toAbsolutePath().toString();
        var remotePath = "/test.txt";

        jSchClient.uploadFile(localPath, remotePath);
        var remoteFile = remoteDirPath.resolve("test.txt");

        assertThat(Files.exists(remoteFile)).isTrue();

        Files.delete(remoteFile);
    }

    @Test
    @DisplayName("should delete remote file")
    void shouldDeleteRemoteFile() {

        fail("Not implemented");
    }
}
