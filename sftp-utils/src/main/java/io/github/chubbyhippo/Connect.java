package io.github.chubbyhippo;

import com.jcraft.jsch.JSchException;

public interface Connect {
    JschClient connect() throws JSchException;
}
