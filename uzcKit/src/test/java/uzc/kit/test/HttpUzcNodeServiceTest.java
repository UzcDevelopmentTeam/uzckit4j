package uzc.kit.test;

import uzc.kit.service.UzcNodeService;
import uzc.kit.service.impl.DefaultSchedulerAssigner;
import uzc.kit.service.impl.HttpUzcNodeService;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HttpUzcNodeServiceTest extends UzcNodeServiceTest {
    @Override
    protected UzcNodeService getUzcNodeService() {
        return new HttpUzcNodeService("https://wallet.uzc-alliance.org:8125", "uzckit4j-TEST", new DefaultSchedulerAssigner());
    }
}
