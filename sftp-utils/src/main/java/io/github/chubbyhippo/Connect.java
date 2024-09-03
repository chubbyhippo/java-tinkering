package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;

public interface Connect {
    JSchClient connect() throws JSchException;
}
