package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.node.entity.Node;

public class Heuristic {
    private Heuristic() {}
    public static final double EARTH_RADIUS_M = 6_371_000.0;
    public static final double FLOOR_HEIGHT_M = 2.3;

    // 정책 튜닝: "가장 싼" 단위 비용(휴리스틱용) — 과대추정 방지
    public static double COST_PER_M_HORIZONTAL_MIN = 1.0;
    public static double COST_PER_M_VERTICAL_MIN   = 0.8; // 엘리베이터가 계단보다 쌀 경우 등

    // 안전한 "가중 맨해튼" 휴리스틱 (권장)
    public static double h(Node a, Node b) {
        double dHoriz = haversineMeters(a.lat(), a.lon(), b.lat(), b.lon());
        double dz = Math.abs(a.floor() - b.floor()) * FLOOR_HEIGHT_M;
        return dHoriz * COST_PER_M_HORIZONTAL_MIN + dz * COST_PER_M_VERTICAL_MIN;
    }

    // 등방성이라면 3D 유클리드도 가능(단위비용 동일 가정): sqrt(d^2 + dz^2)
    public static double h3D(Node a, Node b) {
        double dHoriz = haversineMeters(a.lat(), a.lon(), b.lat(), b.lon());
        double dz = Math.abs(a.floor() - b.floor()) * FLOOR_HEIGHT_M;
        return Math.sqrt(dHoriz*dHoriz + dz*dz);
    }

    public static double haversineMeters(double lat1, double lon1,
                                         double lat2, double lon2) {
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dPhi = Math.toRadians(lat2 - lat1);
        double dLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);

        double c = 2 * Math.asin(Math.sqrt(a)); // central angle (radians)
        return EARTH_RADIUS_M * c;              // arc length (meters)
    }
}
