package cassandra.java.app.demo;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class IPStrippingAddressTranslator implements AddressTranslator {

    private static Logger logger = LoggerFactory.getLogger(IPStrippingAddressTranslator.class);

    public IPStrippingAddressTranslator(DriverContext context) {
    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress address) {
        logger.debug("Stripping IP address of host: {}", address.getHostName());
        return InetSocketAddress.createUnresolved(address.getHostName(), address.getPort());
    }

    @Override
    public void close() {

    }
}
