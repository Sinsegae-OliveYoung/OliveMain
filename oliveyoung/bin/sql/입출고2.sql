
-- ★ role 직급 --------------------------------------------------
CREATE TABLE role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(10) NOT NULL UNIQUE,
    role_name VARCHAR(20) NOT NULL
);

INSERT INTO role (role_code, role_name) VALUES
('TM', '팀장'),
('SM', '점장'),
('ST', '스태프');

select * from role;

-- ★ user 사원 --------------------------------------------------
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_no INT NOT NULL UNIQUE,
    user_name VARCHAR(30) NOT NULL,
    role_id INT NOT NULL,
    tel VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    hiredate DATE NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

INSERT INTO user (user_no, user_name, role_id, tel, email, hiredate) VALUES
(100, '홍길동', 2, '010-2222-3333', 'gildong@google.com', '2021-03-01'),
(101, '문정민', 3, '010-2234-9455', 'jungmin@naver.com', '2024-05-28'),
(102, '김민지', 1, '010-3333-4125', 'minji@daum.net', '2015-12-09')
;

select * from branch;

-- ★ branch 지점--------------------------------------------------
CREATE TABLE branch (
    br_id INT PRIMARY KEY AUTO_INCREMENT,
    br_name VARCHAR(50) NOT NULL,
    br_address VARCHAR(100) NOT NULL,
    br_tel VARCHAR(20) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
)
;

INSERT INTO branch (br_name, br_address, br_tel, user_id) VALUES
('신세계점', '강남구 역삼동', '02-010-1111', 1),
('아이앤씨점', '강남구 논현동', '02-020-2222', 1),
('삼성점', '강남구 삼성동', '02-030-3333', 2),
('봉은사점', '강남구 삼성동', '02-040-4444', 3)
;

select * from branch
;

-- ★ Member 소속 지점--------------------------------------------------
CREATE TABLE member (
    mem_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    br_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (br_id) REFERENCES branch(br_id)
)
;

DELETE *FROM MEMBER;

INSERT INTO member (user_id, br_id) VALUES
(1, 1),
(1, 1),
(2, 2),
(3, 3)
;

select * from member
;

-- ★ bound_state 입출고 승인 상태 --------------------------------------------------
CREATE TABLE bound_state (
    bo_state_id INT PRIMARY KEY AUTO_INCREMENT,
    bo_state_name VARCHAR(50) NOT NULL
)
;

INSERT INTO bound_state (bo_state_name) VALUES
('승인 대기'),
('승인 완료'),
('입고 완료'),
('출고 완료'),
('승인 거부')
;

select * from bound_state
;


-- ★ inbound 입고 요청서 --------------------------------------------------
CREATE TABLE inbound (
    ib_id INT PRIMARY KEY auto_increment,
    user_id INT NOT NULL,
    ib_date DATE NOT NULL,
    comment VARCHAR(500),
    bo_state_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (bo_state_id) REFERENCES bound_state(bo_state_id)
)
;

drop table inbound
;

INSERT INTO inbound (user_id, ib_date, comment, bo_state_id) VALUES
(1, '2025-05-08', '메모1', 1),
(2, '2025-05-01', '메모2', 2),
(3, '2025-04-24', '메모3', 3)
;

select * from inbound_product
;



-- ★ inbound_product 입고 상품 --------------------------------------------------
CREATE TABLE inbound_product (
    ib_pd_id INT PRIMARY KEY AUTO_INCREMENT,
    ib_id INT NOT NULL,
    option_id INT NOT NULL,
    ib_pd_count INT NOT NULL,
    FOREIGN KEY (ib_id) REFERENCES inbound(ib_id),
    FOREIGN KEY (option_id) REFERENCES product_option(option_id)
);

INSERT INTO inbound_product (ib_id, option_id, ib_pd_count) VALUES
(1, 1, 50),
(1, 2, 30),
(1, 6, 20),
(2, 2, 40),
(2, 4, 70),
(3, 1, 20),
(3, 5, 10)
;

select * from inbound_product
;

-- ★ outbound 출고 요청서 --------------------------------------------------


CREATE TABLE outbound (
    ob_id int PRIMARY key auto_increment,
    user_id INT NOT NULL,
    ob_date DATE NOT NULL,
    comment VARCHAR(500),
    bo_state_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (bo_state_id) REFERENCES bound_state(bo_state_id)
);

drop table outbound
;


INSERT INTO outbound (user_id, ob_date, comment, bo_state_id) VALUES
(1, '2025-05-09', '메모1', 1),
(2, '2025-05-02', '메모2', 2),
(3, '2025-04-25', '메모3', 3);

select * from outbound
;

-- ★ outbound_product 출고 상품 --------------------------------------------------
CREATE TABLE outbound_product (
    ob_pd_id INT PRIMARY KEY AUTO_INCREMENT,
    ob_id INT NOT NULL,
    option_id INT NOT NULL,
    ob_pd_count INT NOT NULL,
    FOREIGN KEY (ob_id) REFERENCES outbound(ob_id),
    FOREIGN KEY (option_id) REFERENCES product_option(option_id)
);

INSERT INTO outbound_product (ob_id, option_id, ob_pd_count) VALUES
(1, 1, 10),
(1, 2, 20),
(1, 6, 30),
(2, 2, 10),
(2, 4, 30),
(3, 1, 20),
(3, 5, 30);


select * from outbound_product
;
