package hongik.map.honggildong.global.apiPayload.code.status;

import hongik.map.honggildong.global.apiPayload.code.BaseCode;
import hongik.map.honggildong.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 기본 에러 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    
    // 멤버 관련 응답
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"MEMBER409","이미 존재하는 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER404","존재하지 않는 사용자입니다."),


    // 시설 관련 응답
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND,"FACILITY404","존재하지 않는 시설입니다."),

    //노드 관련 응답
    NODE_NOT_FOUND(HttpStatus.NOT_FOUND,"NODE404","존재하지 않는 노드입니다."),

    // 길찾기 에러 응답
    PATH_NOT_FOUND(HttpStatus.NOT_FOUND, "ROUTE40401", "출발지에서 목적지까지의 경로가 존재하지 않습니다."),
    INVALID_NODE_ID(HttpStatus.BAD_REQUEST, "GRAPH40001", "노드 ID 형식이 올바르지 않습니다."),

    // 즐겨찾기 폴더 에러 응답
    BOOKMARK_FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKMARK404", "즐겨찾기 폴더가 존재하지 않습니다."),

    // 즐겨찾기 시설 에러 응답
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKMARK404", "해당 시설의 즐겨찾기가 존재하지 않습니다."),

    // 빌딩 관련 에러 응답
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "BUILDING404", "건물이 존재하지 않습니다."),

    // S3 관련 에러 응답
    S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3500", "S3에서 정보를 가져오는 중 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
