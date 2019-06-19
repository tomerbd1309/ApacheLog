
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class GeoLocatorTest {
    @Rule
    public final ExpectedException exceptionRule = ExpectedException.none();
    private GeoLocator geoLocator;

    @Before
    public void init() throws IOException {
        geoLocator = new GeoLocator();
    }

    @After
    public void tearDown() throws Exception {
        if (geoLocator != null) {
            geoLocator.close();
        }
    }

    /**
     * Test name format:<br>
     *
     * methodName_testDescription_expectedResult<br>
     * <br>
     * methodName - method that is being tested<br>
     * testDescription - what is the case the method is testing<br>
     * expectedResult - can be return value, exception that is thrown, ect
     */
    @Test
    public void getCountryByIp_ipIsNull_throwsException() throws TaboolaException {
        exceptionRule.expect(TaboolaException.class);
        exceptionRule.expectMessage("The address 127.0.0.1 is not in the database.");

        geoLocator.getCountryByIp(null);
    }

    @Test
    public void getCountryByIp_ipFromUnitedStates_returnUnitedStates() throws TaboolaException {
        final String ip = "65.34.248.51";
        final String country = geoLocator.getCountryByIp(ip);
        assertEquals(country, "United States");
    }
}