package com.example.hatsalvoids.global;

import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

public class GeometryUtils {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 5186);

    public static List<List<double[]>> convertCoordinates2LinearRings(Coordinate[] coordinates){
        // 입력이 null일 경우 빈 리스트를 반환하여 NullPointerException을 방지합니다.
        if (coordinates == null) {
            return new ArrayList<>();
        }

        // 최종적으로 반환될 링(ring)들의 리스트
        List<List<double[]>> rings = new ArrayList<>();

        // 단일 링을 구성할 좌표들의 리스트
        List<double[]> singleRing = new ArrayList<>();

        // Coordinate 배열을 순회하며 double[] 형식으로 변환합니다.
        for (Coordinate coord : coordinates) {
            // 각 좌표를 [x, y] 형태의 double 배열로 만듭니다.
            double[] point = new double[]{coord.getX(), coord.getY()};
            singleRing.add(point);
        }

        // 완성된 단일 링을 최종 리스트에 추가합니다.
        rings.add(singleRing);

        return rings;
    }

    // 다중 폴리곤(내부 구멍 포함) 생성
    public static Polygon createPolygonFromRings(List<List<double[]>> rings) {
        if (rings == null || rings.isEmpty()) {
            throw new IllegalArgumentException("rings must not be null or empty");
        }

        // 첫 번째 LinearRing은 shell (외곽선)
        LinearRing shell = createLinearRing(rings.get(0));

        // 나머지는 holes (구멍)
        LinearRing[] holes = new LinearRing[rings.size() - 1];
        for (int i = 1; i < rings.size(); i++) {
            holes[i - 1] = createLinearRing(rings.get(i));
        }

        // Polygon 객체 생성
        Polygon polygon = new Polygon(shell, holes, geometryFactory);
        polygon.setSRID(5186);
        return polygon;
    }

    // 2D double 배열 좌표로 LinearRing 생성
    private static LinearRing createLinearRing(List<double[]> coords) {
        Coordinate[] coordinates = new Coordinate[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            double[] point = coords.get(i);
            if (point.length < 2) {
                throw new IllegalArgumentException("Each coordinate must have at least 2 dimensions (x, y)");
            }
            coordinates[i] = new Coordinate(point[0], point[1]);
        }

        return geometryFactory.createLinearRing(coordinates);
    }
}
