package octoveau.sso.admin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import octoveau.sso.admin.dto.UserDTO;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

/**
 * User
 *
 * @author yifanzheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class User extends AbstractAuditingEntity {

    private static final long serialVersionUID = 3340373364530753417L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", columnDefinition = "char(11)", nullable = false, unique = true)
    private String phone;

    @Column(name = "nick_name", columnDefinition = "varchar(30)")
    private String nickName;

    @Column(name = "password", columnDefinition = "varchar(68)", nullable = false)
    private String password;

    @Column(name = "active")
    private Boolean active = true;

    public UserDTO toDTO() {
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(this, user);
        return user;
    }

}
