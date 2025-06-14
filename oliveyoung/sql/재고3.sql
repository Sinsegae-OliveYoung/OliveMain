-- ★ approve_state 재고 입출고 승인 상태 ---------------------------------
CREATE TABLE approve_state (
    app_id INT PRIMARY KEY AUTO_INCREMENT,
    app_name VARCHAR(50) NOT NULL
);

INSERT INTO approve_state (app_name) VALUES
('승인 완료'),
('승인 대기');

select * from outbound_approve
;

-- ★ inbound_approve 재고반영 - 입고 승인 테이블 ---------------------------------
CREATE TABLE inbound_approve (
    ia_id INT PRIMARY KEY AUTO_INCREMENT,
    ib_id INT NOT NULL,
    user_id INT NOT NULL,
    app_id INT NOT NULL,
    ia_date DATE NOT NULL,
    FOREIGN KEY (ib_id) REFERENCES inbound(ib_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (app_id) REFERENCES approve_state(app_id)
);

INSERT INTO inbound_approve (ib_id, user_id, app_id, ia_date) VALUES
(1, 1, 1, '2025-05-09'),
(2, 2, 2, '2025-06-20')
;

select * from inbound_approve
;

-- ★ outbound_approve 카테고리 ---------------------------------
CREATE TABLE outbound_approve (
    oa_id INT PRIMARY KEY AUTO_INCREMENT,
    ob_id INT NOT NULL,
    user_id INT NOT NULL,
    app_id INT NOT NULL,
    oa_date DATE NOT NULL,
    FOREIGN KEY (ob_id) REFERENCES outbound(ob_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (app_id) REFERENCES approve_state(app_id)
);

drop table outbound_approve
;
INSERT INTO outbound_approve (ob_id, user_id, app_id, oa_date) VALUES
(1, 1, 1, '2025-05-09'),
(3, 2, 2, '2025-05-12'),
(2, 2, 1, '2025-05-11')
;
select * from outbound_approve
;
-- ★ stock  재고 (branch_id fk 업데이트) ---------------------------------
CREATE TABLE stock (
    st_id INT PRIMARY KEY AUTO_INCREMENT,
    option_id INT NOT NULL,
    br_id INT NOT NULL,
    st_quantity INT NOT NULL,
    st_update DATE NOT NULL,
    FOREIGN KEY (option_id) REFERENCES product_option(option_id),
    FOREIGN KEY (br_id) REFERENCES branch(br_id)
);

INSERT INTO stock (option_id, br_id, st_quantity, st_update) VALUES
(1, 1, 130, '2025-05-14'),
(1, 1, 100, '2025-05-20'),
(2, 1, 50, '2025-05-28'),
(3, 1, 70, '2025-06-12'),
(2, 2, 30, '2025-06-14'),
(4, 1, 7, '2025-03-28'),
(5, 1, 5, '2025-04-12'),
(6, 2, 3, '2025-02-14'),
(4, 2, 20, '2024-03-28'),
(4, 3, 20, '2023-04-28'),
(4, 3, 20, '2022-05-28')
;

select * from stock
;
