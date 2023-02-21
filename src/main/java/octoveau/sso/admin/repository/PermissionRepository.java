package octoveau.sso.admin.repository;

import octoveau.sso.admin.entity.Perm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Perm,Long> {

    List<Perm> findByNameAndParentId(String name, Long id);

    Page<Perm> findAllByParentIdEquals(Long parentId, Pageable pageable);

    Page<Perm> findAllByParentIdAndName(Long parentId, String name, Pageable pageable);

    List<Perm> findAllByParentIdIn(List<Long> ids);

    List<Perm> findAllByIdOrParentId(Long id, Long parentId);

}
