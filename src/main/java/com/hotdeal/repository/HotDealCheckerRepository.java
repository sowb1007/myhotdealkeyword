package com.hotdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotdeal.entity.HotDealKeyword;

public interface HotDealCheckerRepository extends JpaRepository<HotDealKeyword, Long> {
	
}
