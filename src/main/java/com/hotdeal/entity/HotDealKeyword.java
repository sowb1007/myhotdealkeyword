package com.hotdeal.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@SequenceGenerator(name = "keyword_seq_generator", sequenceName = "keyword_seq", initialValue = 1, allocationSize = 1)
@Table(name = "tb_keyword")
public class HotDealKeyword {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, 
    generator = "keyword_seq_generator") 
	@Column(name = "keyword_id", nullable = false, unique = true)
	private Long keywordId;
	
	@Column(name = "keyword_name", nullable = false)
	private String keywordName;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
	private Date createDate;



	public Long getKeywordId() {
		return keywordId;
	}


	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}


	public String getKeywordName() {
		return keywordName;
	}


	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
