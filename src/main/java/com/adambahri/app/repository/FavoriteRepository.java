package com.adambahri.app.repository;

import com.adambahri.app.domain.Favorite;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {
    @Query("select favorite from Favorite favorite where favorite.user.login = ?#{principal.preferredUsername}")
    List<Favorite> findByUserIsCurrentUser();
}
