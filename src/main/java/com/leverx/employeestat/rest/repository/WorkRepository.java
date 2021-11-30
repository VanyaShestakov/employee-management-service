package com.leverx.employeestat.rest.repository;

import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, WorkId> {

    Optional<Work> findWorkById(WorkId workId);

    @Override
    List<Work> findAll();

    void deleteById(WorkId workId);
}
