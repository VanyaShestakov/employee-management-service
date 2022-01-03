package com.leverx.employeestat.rest.repository;

import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, WorkId> {

    Optional<Work> findWorkById(WorkId workId);

    @Override
    List<Work> findAll();

    void deleteById(WorkId workId);

    @Query(nativeQuery = true, value =
            "SELECT * FROM project_employee " +
            "WHERE position_end_date < current_date OR position_start_date > current_date")
    List<Work> findAllAvailableNow();

    @Query(nativeQuery = true, value =
            "SELECT * FROM project_employee " +
            "WHERE position_start_date < current_date and position_end_date < current_date " +
            "or position_start_date > current_date + ?1 and position_end_date > current_date + ?1")
    List<Work> findAllAvailableNext(int days);

}
