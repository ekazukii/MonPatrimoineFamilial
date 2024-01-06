package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.FileInfo;
import fr.cytech.mpf.entity.MsgInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {
    /**
     * Get all the messages of a conversation
     * @param conv the conversation id
     * @return a list of messages
     */
    List<FileInfo> findByConv(Long conv);
}