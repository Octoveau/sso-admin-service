package octoveau.sso.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * UserDTO
 *
 * @author yifanzheng
 */
@Data
public class UserDTO {

    private String phone;

    private String nickName;

    private Boolean active;

    private List<String> roles;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant createDate;

    private String lastModifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant lastModifiedDate;
}
