package com.adambahri.app.repository;

import com.adambahri.app.domain.UserExtended;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserExtended entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtendedRepository extends JpaRepository<UserExtended, Long>, JpaSpecificationExecutor<UserExtended> {}
