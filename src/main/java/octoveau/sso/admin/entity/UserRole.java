package octoveau.sso.admin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * UserRole
 *
 * @author yifanzheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_role")
public class UserRole extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1997955934111931587L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "role", length = 15, nullable = false)
    private String role;
}
