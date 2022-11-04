package octoveau.sso.admin.service;

import octoveau.sso.admin.dto.UserDTO;
import octoveau.sso.admin.entity.User;
import octoveau.sso.admin.entity.UserRole;
import octoveau.sso.admin.exception.AlreadyExistsException;
import octoveau.sso.admin.exception.BadRequestAlertException;
import octoveau.sso.admin.repository.UserRepository;
import octoveau.sso.admin.web.rest.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * UserService
 *
 * @author yifanzheng
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    private static final Pattern phone_pattern = Pattern.compile("^1[34578]\\d{9}$");

    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterRequest dto) {
        Matcher phoneMatcher = phone_pattern.matcher(dto.getPhone());
        if (!phoneMatcher.matches()) {
           throw new BadRequestAlertException("Invalid phone");
        }
        // 预检查用户名是否存在
        Optional<User> userOptional = this.getUserByPhone(dto.getPhone());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsException("Register failed, the user already exist.");
        }
        User user = dto.toEntity();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // 如果预检查没有检查到重复，就利用数据库的完整性检查
            throw new AlreadyExistsException("Register failed, the user already exist.");
        }
    }

    public List<UserDTO> listUsers() {
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }
        return userList.stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public UserDTO getUserInfoByPhone(String phone) throws UsernameNotFoundException {
        Optional<User> userOptional = this.getUserByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with phone: " + phone);
        }
        // 获取用户角色
        List<String> roles = this.listUserRoles(phone);
        // 设置用户信息
        UserDTO userDTO = userOptional.get().toDTO();
        userDTO.setRoles(roles);

        return userDTO;
    }

    public List<String> listUserRoles(String phone) {
        return userRoleService.listUserRoles(phone)
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String phone) {
        // 删除用户角色信息
        userRoleService.deleteByPhone(phone);
        // 删除用户基本信息
        userRepository.deleteByPhone(phone);
    }
}
