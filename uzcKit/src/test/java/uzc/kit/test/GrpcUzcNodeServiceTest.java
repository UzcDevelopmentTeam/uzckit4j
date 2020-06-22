package uzc.kit.test;

import uzc.kit.service.UzcNodeService;
import uzc.kit.service.impl.DefaultSchedulerAssigner;
import uzc.kit.service.impl.GrpcUzcNodeService;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GrpcUzcNodeServiceTest extends UzcNodeServiceTest {
    @Override
    protected UzcNodeService getUzcNodeService() {
        return new GrpcUzcNodeService("localhost:6878", new DefaultSchedulerAssigner());
    }
}
