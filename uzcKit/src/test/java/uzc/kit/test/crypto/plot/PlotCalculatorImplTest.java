package uzc.kit.test.crypto.plot;

import uzc.kit.crypto.UzcCrypto;
import uzc.kit.crypto.plot.PlotCalculator;
import uzc.kit.crypto.plot.impl.PlotCalculatorImpl;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlotCalculatorImplTest extends PlotCalculatorTest {
    @Override
    protected PlotCalculator getPlotCalculator() {
        return new PlotCalculatorImpl(() -> UzcCrypto.getInstance().getShabal256());
    }
}
