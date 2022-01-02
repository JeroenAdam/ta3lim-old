package com.adambahri.app.repository;

import com.adambahri.app.domain.Votes;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Votes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VotesRepository extends JpaRepository<Votes, Long>, JpaSpecificationExecutor<Votes> {
    @Query("select votes from Votes votes where votes.user.login = ?#{principal.preferredUsername}")
    List<Votes> findByUserIsCurrentUser();
}
