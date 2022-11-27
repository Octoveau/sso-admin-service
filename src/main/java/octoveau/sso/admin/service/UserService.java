package octoveau.sso.admin.service;

import octoveau.sso.admin.dto.UserDTO;
import octoveau.sso.admin.entity.User;
import octoveau.sso.admin.entity.UserRole;
import octoveau.sso.admin.exception.AlreadyExistsException;
import octoveau.sso.admin.exception.BadRequestAlertException;
import octoveau.sso.admin.exception.UnauthorizedAccessException;
import octoveau.sso.admin.repository.UserRepository;
import octoveau.sso.admin.web.rest.request.UserPasswordUpdateRequest;
import octoveau.sso.admin.web.rest.request.UserRegisterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SMSService smsService;

    private static final Pattern phone_pattern = Pattern.compile("^1[34578]\\d{9}$");

    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterRequest dto) {
        Matcher phoneMatcher = phone_pattern.matcher(dto.getPhone());
        if (!phoneMatcher.matches()) {
            throw new BadRequestAlertException("Invalid phone");
        }
        // 验证smsCode是否有效
        String smsCodeCache = smsService.getCodeCache(dto.getPhone());
        if (StringUtils.isEmpty(smsCodeCache) || StringUtils.equals(dto.getSmsCode(), smsCodeCache)) {
            throw new UnauthorizedAccessException("Invalid code or expired");
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

    public void updateUserPassword(UserPasswordUpdateRequest passwordResetRequest) {
        String phone = passwordResetRequest.getPhone();
        String smsCode = passwordResetRequest.getSmsCode();
        // 根据登录名获取用户信息
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with phone: " + phone);
        }
        String smsCodeCache = smsService.getCodeCache(phone);
        if (StringUtils.isEmpty(smsCodeCache) || !StringUtils.equals(smsCode, smsCodeCache)) {
            throw new UnauthorizedAccessException("Invalid code or expired");
        }

        User user = userOptional.get();
        user.setPassword(passwordResetRequest.getPassword());
        user.setLastModifiedBy(phone);
        userRepository.save(user);
    }


    public Page<UserDTO> queryUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(User::toDTO);
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<UserDTO> getUserInfoByPhone(String phone) throws UsernameNotFoundException {
        Optional<User> userOptional = this.getUserByPhone(phone);
        return userOptional.map(user -> {
            // 获取用户角色
            List<String> roles = this.listUserRoles(phone);
            // 设置用户信息
            UserDTO userDTO = user.toDTO();
            userDTO.setRoles(roles);
            return userDTO;
        });
    }

    public List<String> listUserRoles(String phone) {
        return userRoleService.listUserRoles(phone).stream()
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
