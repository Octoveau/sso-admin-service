package octoveau.sso.admin.service.mapper;

import octoveau.sso.admin.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import octoveau.sso.admin.web.rest.request.UserRegisterRequest;

/**
 * UserMapper
 *
 * @author yifanzheng
 */
@Service
public class UserMapper {

    public User convertOfUserRegisterDTO(UserRegisterRequest dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);

        return user;
    }
}
