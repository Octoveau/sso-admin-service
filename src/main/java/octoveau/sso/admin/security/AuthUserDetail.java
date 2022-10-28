package octoveau.sso.admin.security;

import octoveau.sso.admin.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * AuthUserDetail
 *
 * @author yifanzheng
 */
public class AuthUserDetail extends User {

    private UserDTO userDetail;

    public AuthUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthUserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserDTO getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDTO userDetail) {
        this.userDetail = userDetail;
    }

}