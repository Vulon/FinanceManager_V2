package FinanceManager_V2.Database.Repositories;

import FinanceManager_V2.Database.Entity.LoggerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoggerRepository extends JpaRepository<LoggerEntry, Long> {

}
