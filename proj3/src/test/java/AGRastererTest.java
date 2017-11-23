import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AGRastererTest extends AGMapTest {
    /**
     * Test the rastering functionality of the student code, by calling their getMapRaster method
     * and ensuring that the resulting map is correct. All of the test data is stored in a
     * TestParameters object that is loaded by the AGMapTest constructor. Note that this test file
     * extends AGMapTest, where most of the interesting stuff happens.
     * @throws Exception
     */
    @Test
    public void testGetMapRaster() throws Exception {
        for (TestParameters p : params) {
            Map<String, Object> studentRasterResult = rasterer.getMapRaster(p.rasterParams);
            checkParamsMap("Returned result differed for input: " + p.rasterParams + ".\n",
                    p.rasterResult, studentRasterResult);
        }
    }

    @Test
    public void htmlTests() {
        Map<String, Double> paramsTestHtml = new HashMap<>();
        paramsTestHtml.put("lrlon", -122.24053369025242);
        paramsTestHtml.put("ullon", -122.24163047377972);
        paramsTestHtml.put("w", 892.0);
        paramsTestHtml.put("h", 875.0);
        paramsTestHtml.put("ullat", 37.87655856892288);
        paramsTestHtml.put("lrlat", 37.87548268822065);

        Map<String, Object> results = rasterer.getMapRaster(paramsTestHtml);
        assertEquals(-122.24212646484375, (double) results.get("raster_ul_lon"), 0.0000001);
        assertEquals(7, results.get("depth"));
        assertEquals(-122.24006652832031, (double) results.get("raster_lr_lon"), 0.0000001);
        assertEquals(37.87538940251607, (double) results.get("raster_lr_lat"), 0.0000001);
        assertEquals(37.87701580361881, (double) results.get("raster_ul_lat"), 0.0000001);
        assertTrue((boolean) results.get("query_success"));
    }
}
