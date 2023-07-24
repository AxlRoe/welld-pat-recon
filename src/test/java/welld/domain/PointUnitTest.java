package welld.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointUnitTest {

    @Test
    @DisplayName("Given vertex with coordinates (2,3) and vertex with coordinates (1,2) get slope 1#1")
    public void givenTwoVertices_calculateSlope() {
        Point v1 = new Point(2,3);
        Point v2 = new Point(1,2);

        String slope = v1.calculateSlope(v2);
        Assertions.assertEquals("1#1", slope);
    }

}
