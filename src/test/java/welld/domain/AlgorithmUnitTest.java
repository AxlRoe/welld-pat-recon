package welld.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
@Slf4j
class AlgorithmUnitTest {

    @Autowired
    private Algorithm algorithm;

    @BeforeEach
    public void setUp () {
        algorithm.delete();
    }

    @Test
    @DisplayName("given two point return one slope")
    public void givenTwoPoints_returnOnlyOneSlope() {
        Map<String, Set<Point>> slopeMap = algorithm.insert(new Point(1,1));
        System.out.println("not emtpy slope " + slopeMap.size());
        Assertions.assertTrue(slopeMap.isEmpty());

        slopeMap = algorithm.insert(new Point(2,2));
        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,2)));
        Assertions.assertTrue(slopeMap.get("1#1").containsAll(points));
    }

    @Test
    @DisplayName("given 3 points on the same line return 1 slope ")
    public void givenThreePointsOnTheSameLine_returnOneSlope () {
        Map<String, Set<Point>> slopeMap = algorithm.insert(new Point(1,1));
        Assertions.assertTrue(slopeMap.isEmpty());

        slopeMap = algorithm.insert(new Point(2,2));
        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,2)));
        Assertions.assertTrue(slopeMap.get("1#1").containsAll(points));

        slopeMap = algorithm.insert(new Point(3,3));
        points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,2), new Point(3,3)));
        Assertions.assertTrue(slopeMap.get("1#1").containsAll(points));
    }

    @Test
    @DisplayName("given 3 points not on the same line return 3 slopes ")
    public void givenThreeNotOnTheSameLine_returnThreeSlopes () {

        Map<String, Set<Point>> slopeMap = null;
        algorithm.insert(new Point(1,3));
        algorithm.insert(new Point(2,1));
        slopeMap = algorithm.insert(new Point(3,2));

        Assertions.assertEquals(3, slopeMap.size());

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(2,1), new Point(1,3)));
        Assertions.assertTrue(slopeMap.get("1#-2").containsAll(points));

        points = new HashSet<>(Arrays.asList(new Point(1,3), new Point(3,2)));
        Assertions.assertTrue(slopeMap.get("-2#1").containsAll(points));

        points = new HashSet<>(Arrays.asList(new Point(2,1), new Point(3,2)));
        Assertions.assertTrue(slopeMap.get("1#1").containsAll(points));
    }

    @Test
    @DisplayName("given a space with 3 points not on the same line and N=2, return 3 lines ")
    public void givenThreeNotOnTheSameLine_returnThreeLines () {
        algorithm.insert(new Point(1,3));
        algorithm.insert(new Point(2,1));
        algorithm.insert(new Point(3,2));
        List<List<Point>> lines = algorithm.getLines(2);

        Assertions.assertEquals(3, lines.size());
        Assertions.assertTrue(lines.stream().map(segment -> {
            Set<Point> vertices = new HashSet<>(segment);
            return vertices.size();
        }).allMatch(lineSize -> lineSize == 2)); // ensure not duplicated point in a line

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,3),
                new Point(2,1), new Point(3,2)));

        Assertions.assertTrue(lines.stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toSet()).containsAll(points)); // merging points of each line in a set, will compose a set with all point in the space

    }

    @Test
    @DisplayName("given a space with 3 points not on the same line and N=3, return no lines ")
    public void givenThreeNotOnTheSameLine_returnNoLines () {
        algorithm.insert(new Point(1,3));
        algorithm.insert(new Point(2,1));
        algorithm.insert(new Point(3,2));
        List<List<Point>> lines = algorithm.getLines(3);

        Assertions.assertTrue(lines.isEmpty());
    }

    @Test
    @DisplayName("given a space with 3 points on the same line and N=3, return one line ")
    public void givenThreePointOnTheSameLine_NEqualsToThree_returnOneLine () {
        algorithm.insert(new Point(1,1));
        algorithm.insert(new Point(2,2));
        algorithm.insert(new Point(3,3));
        List<List<Point>> lines = algorithm.getLines(3);

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,2), new Point(3,3)));
        Assertions.assertEquals(1, lines.size());
        Assertions.assertTrue(new HashSet<>(lines.get(0)).containsAll(points));
    }

    @Test
    @DisplayName("given a space with 3 points on the same line and N=2, return one line ")
    public void givenThreePointOnTheSameLine_NEqualsToTwo_returnOneLine () {
        algorithm.insert(new Point(1,1));
        algorithm.insert(new Point(2,2));
        algorithm.insert(new Point(3,3));
        List<List<Point>> lines = algorithm.getLines(2);

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,2), new Point(3,3)));
        Assertions.assertEquals(1, lines.size());
        Assertions.assertTrue(new HashSet<>(lines.get(0)).containsAll(points));
    }

    @Test
    @DisplayName("given a space with 3 points with same y coordinate and N=2 and N=3, return one line ")
    public void givenThreePointWithSameYcoordinate_NEqualsToTwoAndThree_returnOneLine () {

        algorithm.insert(new Point(1,1));
        algorithm.insert(new Point(2,1));
        algorithm.insert(new Point(3,1));
        List<List<Point>> lines = algorithm.getLines(2);

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,1), new Point(3,1)));
        Assertions.assertEquals(1, lines.size());
        Assertions.assertTrue(new HashSet<>(lines.get(0)).containsAll(points));

        lines = algorithm.getLines(3);

        points = new HashSet<>(Arrays.asList(new Point(1,1), new Point(2,1), new Point(3,1)));
        Assertions.assertEquals(1, lines.size());
        Assertions.assertTrue(new HashSet<>(lines.get(0)).containsAll(points));

    }


    @Test
    @DisplayName("given a space with 3 points with negative and positive coordinate and N=2, return one line ")
    public void givenThreePointWithSameYcoordinate_NEqualsToTwo_returnOneLine () {
        algorithm.insert(new Point(-1,1));
        algorithm.insert(new Point(2,1));
        algorithm.insert(new Point(3,-1));
        List<List<Point>> lines = algorithm.getLines(2);

        Set<Point> points = new HashSet<>(Arrays.asList(new Point(-1,1), new Point(2,1), new Point(3,-1)));
        Assertions.assertEquals(3, lines.size());

        Assertions.assertTrue(lines.stream().map(segment -> {
            Set<Point> vertices = new HashSet<>(segment);
            return vertices.size();
        }).allMatch(lineSize -> lineSize == 2)); // ensure not duplicated point in a line

        Assertions.assertTrue(lines.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()).containsAll(points));

    }

}
