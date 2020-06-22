package uzc.kit.test;

import uzc.kit.service.UzcNodeService;
import uzc.kit.service.impl.CompositeUzcNodeService;
import uzc.kit.service.impl.DefaultSchedulerAssigner;
import uzc.kit.service.impl.GrpcUzcNodeService;
import uzc.kit.service.impl.HttpUzcNodeService;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CompositeUzcNodeServiceTest extends UzcNodeServiceTest {
    @Override
    protected UzcNodeService getUzcNodeService() {
        UzcNodeService http = new HttpUzcNodeService("https://wallet.uzc-alliance.org:8125", "uzckit4j-TEST", new DefaultSchedulerAssigner());
        UzcNodeService grpc = new GrpcUzcNodeService("localhost:6878", new DefaultSchedulerAssigner());
        return new CompositeUzcNodeService(http, grpc);
    }
}
