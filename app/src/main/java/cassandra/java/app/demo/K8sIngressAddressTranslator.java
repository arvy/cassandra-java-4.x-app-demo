package cassandra.java.app.demo;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class K8sIngressAddressTranslator implements AddressTranslator {

    public static final Logger logger = LoggerFactory.getLogger(K8sIngressAddressTranslator.class);
    private DriverContext driverContext;
    private Map<InetSocketAddress,InetSocketAddress> mapping;


    public K8sIngressAddressTranslator(DriverContext driverContext) {
        this.driverContext = driverContext;
        Config config = ConfigFactory.load();
        ConfigObject configObject = config.getObject("address-translator-mapping");
        mapping = new HashMap<>();

        for (String ip :
                configObject.keySet()) {
            String[] sourceComponents = ip.split(":");
            String[] targetComponents = configObject.get(ip).render().replaceAll("\"", "").split(":");
            logger.info("target components: {}, {}", targetComponents[0], targetComponents[1]);
            mapping.put(
                    new InetSocketAddress(sourceComponents[0], Integer.parseInt(sourceComponents[1]) ),
                    new InetSocketAddress(targetComponents[0], Integer.parseInt(targetComponents[1])) );
        }

        logger.info("Loaded mapping: {}", mapping);

    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress address) {

        InetSocketAddress translation = mapping.get(address);

        if( translation != null ){
            logger.info("Translating {} into {}", address, translation );
            return translation;
        }
        logger.info("Returning {} without translation", address.toString());
        return address;
    }

    @Override
    public void close() {

    }
}
