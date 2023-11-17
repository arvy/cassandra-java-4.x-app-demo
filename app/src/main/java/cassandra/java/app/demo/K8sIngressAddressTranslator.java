package cassandra.java.app.demo;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;



public class K8sIngressAddressTranslator implements AddressTranslator {

    public static final Logger logger = LoggerFactory.getLogger(K8sIngressAddressTranslator.class);
    private DriverContext driverContext;


    public K8sIngressAddressTranslator(DriverContext driverContext) {
        this.driverContext = driverContext;
    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress address) {
        if( "192.168.1.1".equals(address.getAddress().toString()) ){
            InetSocketAddress translatedAddress = InetSocketAddress.createUnresolved("", 9042);
            logger.info("Translating {} into {}", address, translatedAddress );
            return translatedAddress;
        }
        return address;
    }

    @Override
    public void close() {

    }
}
