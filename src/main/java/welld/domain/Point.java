package welld.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import welld.dto.PointDTO;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class Point {

    private static final String SEPARATOR = "#";
    private int x;
    private int y;

    public static Point from(PointDTO dto) {
        return new Point(dto.getX(), dto.getY());
    }

    public static PointDTO to(Point point) {
        return new PointDTO(point.getX(), point.getY());
    }

    public String calculateSlope(Point point) {
        int ySlope = point.getY() - this.getY();
        int xSlope = point.getX() - this.getX();
        int gcd = gcd(ySlope, xSlope);
        ySlope /= gcd;
        xSlope /= gcd;

        return xSlope + SEPARATOR + ySlope;
    }

    private int gcd(int p, int q)
    {
        if (q == 0) {
            return p;
        }
        int r = p % q;
        return gcd(q, r);
    }



}
