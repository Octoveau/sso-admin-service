package octoveau.sso.admin.repository;

import octoveau.sso.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

/**
 * UserRepository
 *
 * @author yifanzheng
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Modifying
    void deleteByUserName(String userName);
}
