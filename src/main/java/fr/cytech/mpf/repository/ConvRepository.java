package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.ConvInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvRepository extends JpaRepository<ConvInfo, Long> {
    List<ConvInfo> findByConv(Long conv);
}