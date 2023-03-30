package com.hotdeal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hotdeal.entity.HotDealKeyword;
import com.hotdeal.service.HotDealCheckerService;


@Controller
public class HotDealCheckerController {
	
	@Value("${HotDealCrawling.url}")
	private String Url;
	
	@Value("${HotDealCrawling.subjectClass}")
	private String target;
	
	@Value("${HotDealCrawling.subjectClass.timeClass}")
	private String timeCss;
	
	@Value("${HotDealCrawling.confirmCode}")
	private String confirmCode;
	
	@Value("${HotDealCrawling.uploadLimit}")
	private int uploadLimit;

	@Autowired
	private HotDealCheckerService hotDealCrawlingService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = { "/" })
	public String getCheckList(Model map) {
		log.info("---getCheckList----");
		String keyword = "";
		List<HotDealKeyword> Keywords = hotDealCrawlingService.getAllKeyword();
		List<Map<String, Object>> existKeywords = new ArrayList<Map<String, Object>>();
		
		boolean isExist = false;
		try {
			for (HotDealKeyword hotDealKeyword : Keywords) {
				keyword = hotDealKeyword.getKeywordName();
				log.info("Url : " + Url + keyword);
				Document doc = Jsoup.connect(Url + keyword).get();
				Elements elems = doc.select(target);
				if(!elems.isEmpty()) {
					for (Element e : elems.select(timeCss)) {
						isExist = e.text().contains(":");
						if(isExist) {
							break;
						}
					}
				}
				Map<String, Object> resultMap = new HashMap<>(); 
				resultMap.put("keyword_id", hotDealKeyword.getKeywordId());
				resultMap.put("keyword_name", keyword);
				resultMap.put("is_exist", isExist);
				existKeywords.add(resultMap);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		map.addAttribute("existKeywords", existKeywords);
		map.addAttribute("uploadLimit", uploadLimit);

		return "index";
	}
	
	@PostMapping(value = {"/deleteKeyword"})
	@ResponseBody
	public String deleteKeyword(@RequestBody String data) {
		log.info("---deleteKeyword----");
		log.info("received date : " + data);
		JsonObject returnJson = new JsonObject();
		String receivedCode = "";
		String keywordId = "";
		String resultMessage = "삭제 중 에러가 발생했습니다.";
		try {
			JsonObject jo = (JsonObject)JsonParser.parseString(data);
			receivedCode = jo.get("confirmCode").getAsString();
			keywordId = jo.get("keywordId").getAsString();
			if(!receivedCode.equals(confirmCode)) {
				resultMessage ="틀린 확인코드 입니다.";
			} else {
				hotDealCrawlingService.deleteKeyword(Long.parseLong(keywordId));
				resultMessage = "삭제를 성공했습니다.";
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		returnJson.addProperty("resultMessage", resultMessage);
		log.info("send date : " + returnJson.toString());
		return returnJson.toString();
	}
	
	@PostMapping(value = {"/addKeyword"})
	@ResponseBody
	public String addKeyword(@RequestBody String data) {
		log.info("---addKeyword----");
		log.info("received date : " + data);
		JsonObject returnJson = new JsonObject();
		String receivedCode = "";
		String keywordName = "";
		String resultMessage = "등록 중 에러가 발생했습니다.";
		try {
			JsonObject jo = (JsonObject)JsonParser.parseString(data);
			receivedCode = jo.get("confirmCode").getAsString();
			keywordName = jo.get("keywordName").getAsString();
			if (ObjectUtils.isEmpty(keywordName)) {
				resultMessage =  "키워드는 공란이거나 공백문자로 등록할 수 없습니다.";
			} else if(!receivedCode.equals(confirmCode)) {
				resultMessage =  "틀린 확인코드 입니다.";
			} else if(hotDealCrawlingService.getAllKeywordCount() < 10) {
				resultMessage = "키워드는 30개 이상 등록 하실 수 없습니다.";
			} else {
				HotDealKeyword hotDealKeyword = new HotDealKeyword();
				hotDealKeyword.setKeywordName(keywordName);
				
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				System.out.println(formatter.format(date));
				
				hotDealKeyword.setCreateDate(date);
				hotDealCrawlingService.saveKeyword(hotDealKeyword);
				resultMessage = "등록을 성공했습니다.";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		returnJson.addProperty("resultMessage", resultMessage);
		log.info("send date : " + returnJson.toString());
		return returnJson.toString();
	}
}
