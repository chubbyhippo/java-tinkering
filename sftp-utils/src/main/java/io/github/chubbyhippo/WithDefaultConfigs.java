package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;

public interface WithDefaultConfigs {
    Connect withDefaultConfigs() throws JSchException;
}
