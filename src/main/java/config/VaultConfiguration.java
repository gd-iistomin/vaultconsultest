package config;

import com.bettercloud.vault.VaultConfig;

public class VaultConfiguration {

    private VaultConfiguration() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    private static VaultConfig conf;

    public static VaultConfig getConf() {
        if (conf == null) {
            try {
                conf = new VaultConfig()
                        .address("http://127.0.0.1:8200")
                        .token("myroot")
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create VaultConfig instance!");
            }
        }
        return conf;
    }
}
