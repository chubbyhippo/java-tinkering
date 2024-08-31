package io.github.chubbyhippo;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.readAllBytes;

public class SftpUtils {
    private SftpUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ClientSession createSession(String username, String host, int port, String password) throws IOException {
        try (var client = SshClient.setUpDefaultClient()) {
            client.start();

            var session = client.connect(username, host, port).verify().getSession();
            session.addPasswordIdentity(password);
            session.auth().verify();

            return session;
        }
    }

    public static void upload(String username, String host, int port, String password, Path localFile, String remotePath) throws IOException {
        try (SshClient client = SshClient.setUpDefaultClient();
             ClientSession session = createSession(username, host, port, password);
             SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session)) {

            sftpClient.write(remotePath).write(readAllBytes(localFile));
        }
    }

    public static void download(String username, String host, int port, String password, String remoteFile, Path localPath) throws IOException {
        try (SshClient client = SshClient.setUpDefaultClient();
             ClientSession session = createSession(username, host, port, password);
             SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session)) {

            copy(sftpClient.read(remoteFile), localPath);
        }
    }

    public static void delete(String username, String host, int port, String password, String remoteFile) throws IOException {
        try (SshClient client = SshClient.setUpDefaultClient();
             ClientSession session = createSession(username, host, port, password);
             SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session)) {

            sftpClient.remove(remoteFile);
        }
    }
}
