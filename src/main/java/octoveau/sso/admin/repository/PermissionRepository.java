package octoveau.sso.admin.repository;

import octoveau.sso.admin.entity.Perm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PermissionRepository extends JpaRepository<Perm,Long> {

    List<Perm> findByPermNameAndParentId(String permName, Long id);

    List<Perm> findAllByParentIdIn(List<Long> ids);

    List<Perm> findAllByIdOrParentId(Long id, Long parentId);

    Page<Perm> findAll(Specification<Perm> specification, Pageable pageable);
}
