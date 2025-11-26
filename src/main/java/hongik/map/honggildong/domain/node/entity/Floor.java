package hongik.map.honggildong.domain.node.entity;

public enum Floor {
    NONFLOOR,
    B5,B4,B3,B2,B1,
    LOBBY,
    F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,F13,F14,F15,F16;

    // 층 순서 값을 리턴
    public static int getOrder(Floor floor) {
        return switch (floor) {
            case B5 -> -5;
            case B4 -> -4;
            case B3 -> -3;
            case B2 -> -2;
            case B1 -> -1;
            case LOBBY -> 0;
            case F1 -> 1;
            case F2 -> 2;
            case F3 -> 3;
            case F4 -> 4;
            case F5 -> 5;
            case F6 -> 6;
            case F7 -> 7;
            case F8 -> 8;
            case F9 -> 9;
            case F10 -> 10;
            case F11 -> 11;
            case F12 -> 12;
            default -> 99;
        };
    }
}
