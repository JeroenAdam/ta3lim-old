package com.adambahri.app.repository;

import com.adambahri.app.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    @Query("select message from Message message where message.receiver.login = ?#{principal.preferredUsername}")
    List<Message> findByReceiverIsCurrentUser();

    @Query("select message from Message message where message.sender.login = ?#{principal.preferredUsername}")
    List<Message> findBySenderIsCurrentUser();
}
