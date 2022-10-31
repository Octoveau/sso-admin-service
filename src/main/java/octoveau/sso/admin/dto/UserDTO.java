package octoveau.sso.admin.dto;

import lombok.Data;

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
}
