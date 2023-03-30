CREATE TABLE tb_keyword
( 
    keyword_id        NUMERIC(22,0)	NOT NULL,
    keyword_name      VARCHAR2(255) NOT NULL, 
    create_date       date NOT NULL
);

ALTER TABLE tb_keyword ADD CONSTRAINT keyword_pk PRIMARY KEY (keyword_id);

create SEQUENCE keyword_seq INCREMENT BY 1 NOCYCLE NOCACHE MAXVALUE 99999999999;

select * from tb_keyword order by create_date desc;
 
insert into tb_keyword values(keyword_seq.nextval,'네이버',sysdate);