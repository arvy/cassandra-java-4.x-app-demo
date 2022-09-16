package cassandra.java.app.demo;

import com.datastax.oss.driver.api.core.Version;
import com.datastax.oss.driver.api.core.loadbalancing.NodeDistance;
import com.datastax.oss.driver.api.core.loadbalancing.NodeDistanceEvaluator;
import com.datastax.oss.driver.api.core.metadata.Node;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionDependentNodeDistanceEvaluator implements NodeDistanceEvaluator {

    private Logger logger = LoggerFactory.getLogger(VersionDependentNodeDistanceEvaluator.class);

    private Version acceptVersion;

    public VersionDependentNodeDistanceEvaluator(String localDc, String acceptVersion) {
        this.acceptVersion = Version.parse(acceptVersion);
    }

    @Nullable
    @Override
    public NodeDistance evaluateDistance(@NonNull Node node, @Nullable String localDc) {
        logger.debug("Evaluating node {} with version {}", node.getEndPoint(), node.getCassandraVersion());
        if( node.getCassandraVersion().equals( acceptVersion ) ){
            if( node.getDatacenter().equalsIgnoreCase(localDc) ){
                return NodeDistance.LOCAL;
            }else{
                return NodeDistance.REMOTE;
            }

        } else {
            return NodeDistance.IGNORED;
        }
    }
}
