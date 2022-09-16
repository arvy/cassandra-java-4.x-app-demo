package cassandra.java.app.demo;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class HostnameMappingAddressTranslator implements AddressTranslator {

    public HostnameMappingAddressTranslator() {
    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress address) {
        try {
            if( address.getAddress().equals( InetAddress.getByAddress(new byte[]{10,101,33,(byte)222}) ) ){
                return InetSocketAddress.createUnresolved("ip-10-101-33-222.srv101.dsinternal.org", 9042);
            } else if (address.getAddress().equals( InetAddress.getByAddress(new byte[]{10, 101, 34, (byte)187}) ) ){
                return InetSocketAddress.createUnresolved("ip-10-101-34-187.srv101.dsinternal.org", 9042);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return address;
    }

    @Override
    public void close() {

    }
}
