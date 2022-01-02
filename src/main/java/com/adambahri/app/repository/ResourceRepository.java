package com.adambahri.app.repository;

import com.adambahri.app.domain.Resource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Resource entity.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    @Query("select resource from Resource resource where resource.user.login = ?#{principal.preferredUsername}")
    List<Resource> findByUserIsCurrentUser();

    @Query(
        value = "select distinct resource from Resource resource left join fetch resource.topics left join fetch resource.skills",
        countQuery = "select count(distinct resource) from Resource resource"
    )
    Page<Resource> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct resource from Resource resource left join fetch resource.topics left join fetch resource.skills")
    List<Resource> findAllWithEagerRelationships();

    @Query("select resource from Resource resource left join fetch resource.topics left join fetch resource.skills where resource.id =:id")
    Optional<Resource> findOneWithEagerRelationships(@Param("id") Long id);
}
