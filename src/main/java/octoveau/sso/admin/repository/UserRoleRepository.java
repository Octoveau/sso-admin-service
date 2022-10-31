package octoveau.sso.admin.repository;

import octoveau.sso.admin.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * UserRoleRepository
 *
 * @author yifanzheng
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByPhone(String phone);

    @Modifying
    void deleteByPhone(String phone);
}
