package com.hotdeal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hotdeal.entity.HotDealKeyword;
import com.hotdeal.repository.HotDealCheckerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HotDealCheckerService {
	
	@Autowired
	private HotDealCheckerRepository hotDealCrawlingRepository;

	public void saveKeyword(HotDealKeyword hotDealKeyword) {
		hotDealCrawlingRepository.save(hotDealKeyword);
	}

	public void deleteKeyword(Long id) {
		hotDealCrawlingRepository.deleteById(id);
	}
	
	public List<HotDealKeyword> getAllKeyword() {
		return hotDealCrawlingRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
	}

	public Long getAllKeywordCount() {
		return hotDealCrawlingRepository.count();
	}
}
