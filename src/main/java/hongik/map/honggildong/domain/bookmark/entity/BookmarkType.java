package hongik.map.honggildong.domain.bookmark.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookmarkType {
    FACILITY, BUILDING;

    @JsonCreator
    public static BookmarkType from(String s) {
        return BookmarkType.valueOf(s.trim().toUpperCase());
    }
}
