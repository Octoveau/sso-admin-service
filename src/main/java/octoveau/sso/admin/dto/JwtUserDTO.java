package octoveau.sso.admin.dto;

/**
 * JwtUserDTO
 *
 * @author yifanzheng
 */
public class JwtUserDTO {

    private UserDTO user;

    private String token;

    public JwtUserDTO(String token, UserDTO user) {
        this.user = user;
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
