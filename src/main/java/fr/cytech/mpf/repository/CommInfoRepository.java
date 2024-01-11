package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.CommInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommInfoRepository extends JpaRepository<CommInfo, Long> {
    /**
     * Get all the comments of a conversation
     * @param conv the conversation id
     * @param souvenir the souvenir id
     * @return a list of comments
     */
    List<CommInfo> findCommInfoByConvAndSouvenir(Long conv, Long souvenir);
    List<CommInfo> findCommInfoBySouvenir(Long souvenir);
}