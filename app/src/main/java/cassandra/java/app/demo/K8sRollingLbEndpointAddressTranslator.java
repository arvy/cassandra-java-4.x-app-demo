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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class K8sRollingLbEndpointAddressTranslator implements AddressTranslator {

    public static final Logger logger = LoggerFactory.getLogger(K8sRollingLbEndpointAddressTranslator.class);
    private DriverContext driverContext;
    private LinkedList<InetSocketAddress> mapping;


    public K8sRollingLbEndpointAddressTranslator(DriverContext driverContext) {
        this.driverContext = driverContext;
        Config config = ConfigFactory.load();
        mapping = new LinkedList<>();

        for (String ip :
                config.getStringList("address-translator.lb-endpoints")) {
            String[] addressComponents = ip.split(":");
            mapping.add(
                    new InetSocketAddress(addressComponents[0], Integer.parseInt(addressComponents[1]) ) );
        }

        logger.info("Loaded LB endpoints: {}", mapping);

    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress address) {

        InetSocketAddress translation = mapping.removeFirst();
        mapping.addLast(translation);

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
