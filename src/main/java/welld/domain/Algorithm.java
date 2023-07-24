package welld.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Algorithm {

    private final Map<String, Set<Point>> linesMap = new HashMap<>();
    private final List<Point> space = new ArrayList<>();

    public boolean isPointAlreadyPresent(Point pointToCheck) {
        return space.stream().anyMatch(point -> point.equals(pointToCheck));
    }
    public Map<String, Set<Point>> insert(Point point) {

        for (Point insertedPoint : space) {
            String slope = point.calculateSlope(insertedPoint);
            linesMap.computeIfAbsent(slope, k -> new HashSet<>()).add(insertedPoint);
            linesMap.computeIfPresent(slope, (k, points) -> {
                points.add(point);
                return points;
            });
            log.info("Line {} connect #{} points", slope, linesMap.get(slope).size());
        }

        space.add(point);
        log.info("Inserted point {}", point);
        return linesMap;
    }

    public List<List<Point>> getLines(Integer n) {
        List<List<Point>> lines = new ArrayList<>();
        for (Set<Point> points: linesMap.values()) {
            if (points.size() >= n) {
                List<Point> line = new ArrayList<>(points).stream().sorted(Comparator.comparing(Point::getX)).collect(Collectors.toList());
                lines.add(line);
            }
        }
        return lines;
    }

    public List<Point> getSpace() {
        return space;
    }

    public void delete() {
        space.clear();
        linesMap.clear();
        log.info("Space deleted");
    }
}
