package config;

import com.ecwid.consul.v1.ConsulClient;

public class ConsulConfiguration {

    private ConsulConfiguration() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static ConsulClient getClient() {
        return new ConsulClient("localhost");
    }
}
