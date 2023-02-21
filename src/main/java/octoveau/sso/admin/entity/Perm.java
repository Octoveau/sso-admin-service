package octoveau.sso.admin.entity;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import octoveau.sso.admin.vo.PermissionItemInfoVO;
import octoveau.sso.admin.vo.PermissionTreeVO;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author cyy
 * @since 2023-02-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "perm")
@ApiModel(value="Perm", description="权限基本信息")
public class Perm extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(30)", nullable = false)
    private String name;

    @Column(name = "action", columnDefinition = "varchar(10)")
    private String action;

    @Column(name = "perm_value", columnDefinition = "varchar(10)")
    private Integer permValue;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "remark", columnDefinition = "varchar(50)")
    private String remark;

    public PermissionTreeVO toPermTreeVO() {
        PermissionTreeVO permissionTreeVO = new PermissionTreeVO();
        BeanUtils.copyProperties(this, permissionTreeVO);
        return permissionTreeVO;
    }

    public PermissionItemInfoVO toPermInfoVO() {
        PermissionItemInfoVO permissionVO = new PermissionItemInfoVO();
        BeanUtils.copyProperties(this, permissionVO);
        return permissionVO;
    }

}
