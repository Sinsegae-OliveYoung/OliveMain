SELECT 	b.br_id
	   ,b.br_name 
	   ,u.user_id 
	   ,u.user_no
	   ,u.user_name 
FROM	user u
	   ,branch b
WHERE 	1 = 1
AND		u.br_id = b.br_id
order by b.br_id, u.user_id
;


-- CREATE TABLE user (
	 user_id int PRIMARY KEY AUTO_INCREMENT
	,user_no int
	,user_name varchar(20)
	,br_id int
	,constraint fk_user_branch_user foreign key(br_id)
				references branch(br_id)
);

-- CREATE TABLE branch (
	 br_id int PRIMARY KEY AUTO_INCREMENT
	,br_name varchar(20)
);

select * from user;
INSERT INTO shop.user (user_no, user_name, br_id)
VALUES('101', '김민지', 1);
INSERT INTO shop.user (user_no, user_name, br_id)
VALUES('102', '김민정', 2);
INSERT INTO shop.user (user_no, user_name, br_id)
VALUES('103', '카리나', 2);
INSERT INTO shop.user (user_no, user_name, br_id)
VALUES('104', '장원영', 2);

select * from branch;

INSERT INTO shop.branch (br_name)
VALUES('신세계점');

INSERT INTO shop.branch (br_name)
VALUES('삼성점');

-- --------------------------------------------------
create database olive; 

-- --------------------------------------------------
-- 상품 테이블
-- --------------------------------------------------

-- ★ category 카테고리 ---------------------------------
CREATE TABLE category (
    ct_id INT PRIMARY KEY AUTO_INCREMENT,
    ct_code VARCHAR(10) NOT NULL UNIQUE,
    ct_name VARCHAR(20) NOT NULL
);

drop table category;

INSERT INTO category (ct_code, ct_name) VALUES
('sk', '스킨케어'),
('ms', '마스크팩'),
('cl', '클렌징'),
('su', '선케어'),
('mk', '메이크업'),
('to', '뷰티소품'),
('hr', '헤어케어'),
('bd', '바디케어'),
('pf', '향수/디퓨저'),
('na', '네일');

select	*
from	category
;

-- ★ 상세 카테고리 category_detail--------------------------------------------------
CREATE TABLE category_detail (
    ct_dt_id INT PRIMARY KEY AUTO_INCREMENT,
    ct_dt_code VARCHAR(10) NOT NULL UNIQUE,
    ct_id INT NOT NULL,
    ct_dt_name VARCHAR(20) NOT NULL,
    FOREIGN KEY (ct_id) REFERENCES category(ct_id)
);

drop table category_detail;

-- 데이터 삽입
INSERT INTO category_detail (ct_dt_code, ct_id, ct_dt_name) VALUES
('tn', 1, '토너'),
('es', 1, '에센스'),
('cr', 1, '크림'),
('lo', 1, '로션'),
('cu', 5, '쿠션'),
('tt', 5, '틴트');

select	*
from	category_detail
;

-- ★ brand 브랜드 --------------------------------------------------
CREATE TABLE brand (
    bd_id INT PRIMARY KEY AUTO_INCREMENT,
    bd_code VARCHAR(10) NOT NULL UNIQUE,
    bd_name VARCHAR(50) NOT NULL
);

-- 데이터 삽입
INSERT INTO brand (bd_code, bd_name) VALUES
	('espa', '에스쁘아'),
	('tnmr', '토니모리'),
	('clio', '클리오')
;

select	*
from	brand
;

-- ★ product 상품 --------------------------------------------------

CREATE TABLE product (
     product_id INT PRIMARY KEY auto_increment
    ,product_name VARCHAR(100) NOT null
    ,ct_id INT NOT null
    ,ct_dt_id INT NOT null
    ,bd_id INT NOT null
    ,FOREIGN KEY (ct_id) REFERENCES category(ct_id)
    ,FOREIGN KEY (ct_dt_id) REFERENCES category_detail(ct_dt_id)
    ,FOREIGN KEY (bd_id) REFERENCES brand(bd_id)
);

drop table product
;
-- 데이터 삽입
INSERT INTO product (product_name, ct_id, ct_dt_id, bd_id) VALUES
('토니모리 어성초 에센스', 1, 2, 2),
('에스쁘아 비벨벳 쿠션', 5, 5, 1),
('클리오 크리스탈글램 틴트', 5, 6, 3)
;

select * from product p 
;

-- ★ product_option 상품 옵션 --------------------------------------------------
CREATE TABLE product_option (
    option_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    option_no INT NOT NULL,
    option_name VARCHAR(30),
    option_code VARCHAR(50),
    price INT NOT NULL,
    option_active ENUM('y', 'n') NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
)
;

drop table product_option
;

INSERT INTO product_option (option_code, product_id, option_no, option_name, price, option_active) VALUES
('sk-es-tnmr-1299', 1, 99, '', 18000, 'y'),
('mk-cu-espa-5501', 2, 1, '17호', 21000, 'n'),
('mk-cu-espa-5502', 2, 2, '21호', 21000, 'y'),
('mk-cu-espa-5503', 2, 3, '23호', 21000, 'y'),
('mk-tt-clio-5601', 3, 1, 'red', 12000, 'y'),
('mk-tt-clio-5602', 3, 2, 'pink', 12000, 'y'),
('mk-tt-clio-5603', 3, 3, 'orange', 12000, 'n')
;

select * from product_option
;






