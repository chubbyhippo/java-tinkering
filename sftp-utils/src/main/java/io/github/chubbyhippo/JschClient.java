package io.github.chubbyhippo;

import com.jcraft.jsch.*;

import java.util.List;

public class JschClient {
    private String username;
    private String password;
    private String host;
    private int port;
    private Session session;
    private ChannelSftp channelSftp;

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

    public JschClient withDefaultConfigs() throws JSchException {
        var jSch = new JSch();
        session = jSch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("server_host_key", session.getConfig("server_host_key") + ",ssh-rsa");
        session.setConfig("PubkeyAcceptedAlgorithms", session.getConfig("PubkeyAcceptedAlgorithms") + ",ssh-rsa");
        session.setPassword(password);
        return this;
    }

    public JschClient connect() throws JSchException {
        session.connect();
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        return this;
    }

    public List<String> listFiles(String remoteDir) throws SftpException {

        return channelSftp.ls(remoteDir).stream()
                .filter(l -> !l.getAttrs().isDir())
                .map(ChannelSftp.LsEntry::getFilename)
                .toList();

    }
}
