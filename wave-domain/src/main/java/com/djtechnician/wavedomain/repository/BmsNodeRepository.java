package com.djtechnician.wavedomain.repository;

import com.djtechnician.wavedomain.entity.BmsNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BmsNodeRepository extends JpaRepository<BmsNode, Long> {

}
