package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.CommInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommInfoRepository extends JpaRepository<CommInfo, Long> {
    List<CommInfo> findCommInfoByConvAndSouvenir(Long conv, Long souvenir);
}