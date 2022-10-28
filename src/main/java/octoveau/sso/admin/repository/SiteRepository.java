package octoveau.sso.admin.repository;

import octoveau.sso.admin.entity.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * SiteRepository
 *
 * @author yifanzheng
 */
public interface SiteRepository extends JpaRepository<Site, Long> {

    Optional<Site> findBySiteKey(String siteKey);

    Page<Site> findAllBySiteNameLike(String siteName, Pageable pageable);
}
