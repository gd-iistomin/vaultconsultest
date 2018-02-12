import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;
import config.ConsulConfiguration;
import config.VaultConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws VaultException {
        Vault vault = new Vault(VaultConfiguration.getConf());
        ConsulClient consul = ConsulConfiguration.getClient();

        System.out.println("CONSUL TEST");

        byte[] binaryData = new byte[] {1,2,3,4,5,6,7};
        consul.setKVBinaryValue("someKey", binaryData);

        consul.setKVValue("com.my.app.foo", "foo");
        consul.setKVValue("com.my.app.bar", "bar");
        consul.setKVValue("com.your.app.foo", "hello");
        consul.setKVValue("com.your.app.bar", "world");

        // get single KV for key
        Response<GetValue> keyValueResponse = consul.getKVValue("com.my.app.foo");
        System.out.println(keyValueResponse.getValue().getKey() + ": " + keyValueResponse.getValue().getDecodedValue()); // prints "com.my.app.foo: foo"

        // get list of KVs for key prefix (recursive)
        Response<List<GetValue>> keyValuesResponse = consul.getKVValues("com.my");
        keyValuesResponse.getValue().forEach(value -> System.out.println(value.getKey() + ": " + value.getDecodedValue())); // prints "com.my.app.foo: foo" and "com.my.app.bar: bar"

        //list known datacenters
        Response<List<String>> response = consul.getCatalogDatacenters();
        System.out.println("Datacenters: " + response.getValue());

        // register new service
        NewService newService = new NewService();
        newService.setId("myapp_01");
        newService.setName("myapp");
        newService.setTags(Arrays.asList("EU-West", "EU-East"));
        newService.setPort(8080);
        consul.agentServiceRegister(newService);

        // register new service with associated health check
        NewService newService2 = new NewService();
        newService2.setId("myapp_02");
        newService2.setTags(Collections.singletonList("EU-East"));
        newService2.setName("myapp");
        newService2.setPort(8080);

        NewService.Check serviceCheck = new NewService.Check();
        serviceCheck.setScript("/usr/bin/some-check-script");
        serviceCheck.setInterval("10s");
        newService2.setCheck(serviceCheck);

        // configure 'enable_script_checks' to true'
//        consul.agentServiceRegister(newService2);

        // query for healthy services based on name (returns myapp_01 and myapp_02 if healthy)
        Response<List<HealthService>> healthyServices = consul.getHealthServices("myapp", true, QueryParams.DEFAULT);

        // query for healthy services based on name and tag (returns myapp_01 if healthy)
        Response<List<HealthService>> healthyServices2 = consul.getHealthServices("myapp", "EU-West", true, QueryParams.DEFAULT);

        //Vault test
        System.out.println("VAULT TEST");

        final Map<String, Object> secrets = new HashMap<>();
        secrets.put("value", "world");
        secrets.put("other_value", "You can store multiple name/value pairs under a single key");

        // Write operation
        final LogicalResponse writeResponse = vault.logical()
                .write("secret/hello", secrets);

        // Read operation
        final String value = vault.logical()
                .read("secret/hello")
                .getData().get("value");
        System.out.println(value);
    }

}
