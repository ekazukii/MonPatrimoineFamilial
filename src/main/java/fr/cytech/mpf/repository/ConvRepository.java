package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.MsgInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvRepository extends JpaRepository<MsgInfo, Long> {
    /**
     * Get all the messages of a conversation
     * @param conv the conversation id
     * @return a list of messages
     */
    List<MsgInfo> findByConv(Long conv);
    @Query("SELECT m FROM MsgInfo m WHERE m.user_id IN :userIds")
    List<MsgInfo> findAllByUser_idIn(@Param("userIds") List<Long> userIds);
}