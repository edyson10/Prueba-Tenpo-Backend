package com.tenpo.prueba_tenpo.Repository;

import com.tenpo.prueba_tenpo.Entity.CallHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallHistoryRepository extends JpaRepository<CallHistoryEntity, Long> {
}
