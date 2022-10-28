package octoveau.sso.admin.dto;

import lombok.Data;

/**
 * ResponseDTO
 *
 * @author yifanzheng
 */
@Data
public class ResponseDTO<T> {

    protected Integer code;

    protected String message;

    protected T data;

    public ResponseDTO() {

    }

    public ResponseDTO(T data, Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(data, 200, "Success");
    }

    public static <T> ResponseDTO<T> ok() {
        return new ResponseDTO<>(null, 200, "Success");
    }

}