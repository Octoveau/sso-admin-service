package octoveau.sso.admin.service;

import octoveau.sso.admin.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import octoveau.sso.admin.repository.UserRoleRepository;

import java.util.List;

/**
 * UserRoleService
 *
 * @author yifanzheng
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> listUserRoles(String userName) {
        return userRoleRepository.findByUserName(userName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserName(String userName) {
        userRoleRepository.deleteByUserName(userName);
    }
}
