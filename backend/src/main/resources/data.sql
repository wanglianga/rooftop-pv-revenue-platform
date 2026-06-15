-- =============================================
-- 光伏平台数据初始化脚本
-- 使用说明：
-- 1. 确保数据库 pv_platform 已创建
-- 2. 确保表结构已通过 JPA ddl-auto 创建
-- 3. 执行此脚本插入初始化数据
-- =============================================

-- 清空现有数据（按外键依赖顺序）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE warranty;
TRUNCATE TABLE revenue_allocation;
TRUNCATE TABLE grid_application;
TRUNCATE TABLE construction_record;
TRUNCATE TABLE owner_notice;
TRUNCATE TABLE vote_record;
TRUNCATE TABLE vote_topic;
TRUNCATE TABLE power_curve;
TRUNCATE TABLE meter_reading;
TRUNCATE TABLE pv_string;
TRUNCATE TABLE inverter;
TRUNCATE TABLE roof_area;
TRUNCATE TABLE sys_user;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 1. 系统用户
-- =============================================
INSERT INTO sys_user (username, password, real_name, phone, role, status, create_time) VALUES
('admin', '123456', '系统管理员', '13800138000', 'ADMIN', 1, NOW()),
('owner1', '123456', '张三', '13800138001', 'OWNER', 1, NOW()),
('owner2', '123456', '李四', '13800138002', 'OWNER', 1, NOW()),
('owner3', '123456', '王五', '13800138003', 'OWNER', 1, NOW());

-- =============================================
-- 2. 屋顶区域
-- =============================================
INSERT INTO roof_area (building_no, area_name, area_size, owner_type, usable, description, create_by, create_time) VALUES
('1号楼', '东单元屋顶', 120.5, '公共区域', 1, '1号楼东单元屋顶，可安装光伏组件', 1, NOW()),
('2号楼', '西单元屋顶', 150.0, '业主共有', 1, '2号楼西单元屋顶，可安装光伏组件', 1, NOW()),
('3号楼', '整体屋顶', 200.3, '公共区域', 1, '3号楼整体屋顶，可安装光伏组件', 1, NOW()),
('4号楼', 'A区屋顶', 180.8, '业主共有', 1, '4号楼A区屋顶，可安装光伏组件', 1, NOW()),
('5号楼', 'B区屋顶', 95.6, '公共区域', 1, '5号楼B区屋顶，可安装光伏组件', 1, NOW());

-- =============================================
-- 3. 逆变器
-- =============================================
INSERT INTO inverter (roof_area_id, inverter_code, model, capacity, install_date, status, create_by, create_time) VALUES
(1, 'INV-001', 'SUN-20K', 20.0, DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(2, 'INV-002', 'SUN-30K', 30.0, DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(3, 'INV-003', 'SUN-25K', 25.0, DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(4, 'INV-004', 'SUN-40K', 40.0, DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(5, 'INV-005', 'SUN-15K', 15.0, DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW());

-- =============================================
-- 4. 光伏组串
-- =============================================
INSERT INTO pv_string (roof_area_id, string_code, panel_count, panel_model, install_date, status, create_by, create_time) VALUES
(1, 'STR-001', 20, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(2, 'STR-002', 22, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(3, 'STR-003', 18, 'JKM405N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(4, 'STR-004', 24, 'JKM405N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(5, 'STR-005', 20, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(1, 'STR-006', 22, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(2, 'STR-007', 18, 'JKM405N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(3, 'STR-008', 24, 'JKM405N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(4, 'STR-009', 20, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW()),
(5, 'STR-010', 22, 'JKM395N', DATE_SUB(NOW(), INTERVAL 90 DAY), 1, 1, NOW());

-- =============================================
-- 5. 投票主题
-- =============================================
INSERT INTO vote_topic (title, content, vote_rule, status, start_date, end_date, create_by, create_time) VALUES
('关于安装屋顶光伏系统的提案', '为充分利用小区屋顶资源，降低业主用电成本，拟在小区公共屋顶安装光伏发电系统。预计装机容量100KW，投资约50万元，预计年发电量约12万度，投资回收期约6-8年。', '双三分之二原则：需经参与表决专有部分面积四分之三以上的业主且参与表决人数四分之三以上的业主同意', 1, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, NOW()),
('关于光伏系统维护方案的投票', '为确保光伏系统长期稳定运行，拟委托专业公司进行年度维护，维护费用约5000元/年，从公共收益中支出。', '简单多数原则：参与表决人数过半且同意人数过半即通过', 0, DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 21 DAY), 1, NOW()),
('关于光伏收益分配方式的调整', '鉴于部分业主屋顶面积差异较大，拟调整收益分配方式，从按户平均分配调整为按屋顶面积比例分配。', '双三分之二原则：需经参与表决专有部分面积四分之三以上的业主且参与表决人数四分之三以上的业主同意', 2, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 1, NOW());

-- =============================================
-- 6. 投票记录
-- =============================================
INSERT INTO vote_record (topic_id, owner_id, vote_result, vote_time, remark) VALUES
(1, 2, '同意', DATE_SUB(NOW(), INTERVAL 5 DAY), '支持绿色能源'),
(1, 3, '同意', DATE_SUB(NOW(), INTERVAL 5 DAY), '支持绿色能源'),
(1, 4, '同意', DATE_SUB(NOW(), INTERVAL 4 DAY), '支持绿色能源'),
(1, 2, '同意', DATE_SUB(NOW(), INTERVAL 4 DAY), '支持绿色能源'),
(1, 3, '同意', DATE_SUB(NOW(), INTERVAL 3 DAY), '支持绿色能源'),
(1, 4, '反对', DATE_SUB(NOW(), INTERVAL 3 DAY), '担心安全问题'),
(1, 2, '弃权', DATE_SUB(NOW(), INTERVAL 3 DAY), ''),
(1, 3, '同意', DATE_SUB(NOW(), INTERVAL 2 DAY), '支持绿色能源'),
(1, 4, '同意', DATE_SUB(NOW(), INTERVAL 2 DAY), '支持绿色能源'),
(1, 2, '同意', DATE_SUB(NOW(), INTERVAL 1 DAY), '支持绿色能源');

-- =============================================
-- 7. 业主公告
-- =============================================
INSERT INTO owner_notice (title, content, notice_type, target_audience, publish_time, create_by, create_time) VALUES
('关于屋顶光伏系统施工的通知', '各位业主：小区公共屋顶光伏系统将于2024年1月15日开始施工，预计工期30天。施工期间请注意安全，禁止进入施工区域。如有疑问请联系物业。', '施工通知', '全体业主', DATE_SUB(NOW(), INTERVAL 10 DAY), 1, NOW()),
('光伏系统并网申请已受理', '各位业主：小区光伏系统并网申请已获供电局受理，预计2024年2月完成并网。并网后将按照国家相关政策享受上网电价补贴。', '重要通知', '全体业主', DATE_SUB(NOW(), INTERVAL 5 DAY), 1, NOW()),
('2024年1月发电量统计', '各位业主：2024年1月光伏发电总量为12500度，其中自用8500度，上网4000度。本月收益约850元，将纳入业主公共收益账户。', '月度报告', '全体业主', DATE_SUB(NOW(), INTERVAL 1 DAY), 1, NOW());

-- =============================================
-- 8. 施工记录
-- =============================================
INSERT INTO construction_record (roof_area_id, stage, record_content, operator, operation_time, images, remark, create_by, create_time) VALUES
(1, '勘察设计', '完成屋顶现场勘察，确定安装方案', '王工程师', DATE_SUB(NOW(), INTERVAL 60 DAY), '', '', 1, NOW()),
(1, '基础施工', '完成混凝土基础浇筑，养护7天', '李施工队', DATE_SUB(NOW(), INTERVAL 52 DAY), '', '', 1, NOW()),
(1, '支架安装', '完成光伏支架安装，检查稳固性', '张安装队', DATE_SUB(NOW(), INTERVAL 44 DAY), '', '', 1, NOW()),
(1, '组件安装', '完成光伏组件安装，共200块', '张安装队', DATE_SUB(NOW(), INTERVAL 36 DAY), '', '', 1, NOW()),
(1, '电气安装', '完成逆变器、电表等电气设备安装接线', '赵电工', DATE_SUB(NOW(), INTERVAL 28 DAY), '', '', 1, NOW()),
(1, '系统调试', '系统通电调试，各项参数正常', '刘技术员', DATE_SUB(NOW(), INTERVAL 20 DAY), '', '', 1, NOW()),
(1, '竣工验收', '通过供电局验收，具备并网条件', '供电局', DATE_SUB(NOW(), INTERVAL 12 DAY), '', '', 1, NOW());

-- =============================================
-- 9. 并网申请
-- =============================================
INSERT INTO grid_application (roof_area_id, applicant, application_date, status, grid_no, acceptance_opinion, feedback_by, feedback_time, create_by, create_time) VALUES
(1, '业主委员会', DATE_SUB(NOW(), INTERVAL 30 DAY), '已通过', 'GD-2024-00123', '申请材料齐全，符合并网条件，同意并网', 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 1, NOW()),
(2, '业主委员会', DATE_SUB(NOW(), INTERVAL 20 DAY), '审核中', '', '', NULL, NULL, 1, NOW());

-- =============================================
-- 10. 质保信息
-- =============================================
INSERT INTO warranty (device_type, device_id, device_code, warranty_start, warranty_end, warranty_content, supplier, contact_phone, create_by, create_time) VALUES
('INVERTER', 1, 'INV-001', DATE_SUB(NOW(), INTERVAL 90 DAY), DATE_ADD(NOW(), INTERVAL 1825 DAY), '整机5年质保，免费维修更换', '阳光电源股份有限公司', '400-888-8888', 1, NOW()),
('PV_PANEL', 1, 'STR-001', DATE_SUB(NOW(), INTERVAL 90 DAY), DATE_ADD(NOW(), INTERVAL 9125 DAY), '产品材料和工艺质保10年，功率质保25年', '晶科能源股份有限公司', '400-666-6666', 1, NOW()),
('INVERTER', 2, 'INV-002', DATE_SUB(NOW(), INTERVAL 90 DAY), DATE_ADD(NOW(), INTERVAL 1825 DAY), '整机5年质保，免费维修更换', '阳光电源股份有限公司', '400-888-8888', 1, NOW());

-- =============================================
-- 11. 电表读数（30天 x 5台逆变器）
-- =============================================
-- 使用存储过程生成30天的电表读数数据
DELIMITER //
DROP PROCEDURE IF EXISTS generate_meter_readings //
CREATE PROCEDURE generate_meter_readings()
BEGIN
    DECLARE inv_id INT DEFAULT 1;
    DECLARE day_offset INT DEFAULT 0;
    DECLARE base_reading DOUBLE DEFAULT 1000.0;
    
    WHILE inv_id <= 5 DO
        SET day_offset = 0;
        SET base_reading = 1000.0;
        WHILE day_offset < 30 DO
            INSERT INTO meter_reading (inverter_id, reading_date, generation, meter_reading, reading_by, create_time)
            VALUES (
                inv_id,
                DATE_SUB(NOW(), INTERVAL day_offset DAY),
                100.0 + RAND() * 50,
                base_reading,
                1,
                NOW()
            );
            SET base_reading = base_reading + 100.0 + RAND() * 50;
            SET day_offset = day_offset + 1;
        END WHILE;
        SET inv_id = inv_id + 1;
    END WHILE;
END //
DELIMITER ;

CALL generate_meter_readings();
DROP PROCEDURE IF EXISTS generate_meter_readings;

-- =============================================
-- 12. 功率曲线（每小时数据 x 5台逆变器 x 13小时）
-- =============================================
DELIMITER //
DROP PROCEDURE IF EXISTS generate_power_curves //
CREATE PROCEDURE generate_power_curves()
BEGIN
    DECLARE inv_id INT DEFAULT 1;
    DECLARE hour INT DEFAULT 6;
    DECLARE power_factor DOUBLE;
    DECLARE base_date DATE DEFAULT DATE_SUB(NOW(), INTERVAL 1 DAY);
    
    WHILE inv_id <= 5 DO
        SET hour = 6;
        WHILE hour <= 18 DO
            SET power_factor = SIN(PI() * (hour - 6) / 12);
            INSERT INTO power_curve (inverter_id, record_time, power, voltage, current, temperature, create_time)
            VALUES (
                inv_id,
                DATE_ADD(base_date, INTERVAL hour HOUR),
                5.0 * power_factor + RAND() * 0.5,
                220.0 + RAND() * 10,
                20.0 * power_factor + RAND() * 2,
                25.0 + power_factor * 20 + RAND() * 5,
                NOW()
            );
            SET hour = hour + 1;
        END WHILE;
        SET inv_id = inv_id + 1;
    END WHILE;
END //
DELIMITER ;

CALL generate_power_curves();
DROP PROCEDURE IF EXISTS generate_power_curves;

-- =============================================
-- 13. 收益分配（2个周期 x 3个业主）
-- =============================================
DELIMITER //
DROP PROCEDURE IF EXISTS generate_revenue_allocations //
CREATE PROCEDURE generate_revenue_allocations()
BEGIN
    DECLARE period_idx INT DEFAULT 0;
    DECLARE owner_idx INT DEFAULT 0;
    DECLARE period_str VARCHAR(20);
    DECLARE total_rev DOUBLE;
    DECLARE buildings VARCHAR(20);
    DECLARE owners VARCHAR(20);
    DECLARE owner_id BIGINT;
    DECLARE ratio DOUBLE;
    
    WHILE period_idx < 2 DO
        IF period_idx = 0 THEN
            SET period_str = '2023-12';
        ELSE
            SET period_str = '2024-01';
        END IF;
        
        SET total_rev = 1500.0 + RAND() * 500;
        
        SET owner_idx = 0;
        WHILE owner_idx < 3 DO
            CASE owner_idx
                WHEN 0 THEN
                    SET buildings = '1号楼';
                    SET owners = '张三';
                    SET owner_id = 2;
                    SET ratio = 0.35;
                WHEN 1 THEN
                    SET buildings = '2号楼';
                    SET owners = '李四';
                    SET owner_id = 3;
                    SET ratio = 0.35;
                ELSE
                    SET buildings = '3号楼';
                    SET owners = '王五';
                    SET owner_id = 4;
                    SET ratio = 0.30;
            END CASE;
            
            INSERT INTO revenue_allocation (period, building_no, total_revenue, allocated_amount, allocation_rule, owner_name, owner_id, area_ratio, amount, status, create_by, create_time)
            VALUES (
                period_str,
                buildings,
                total_rev,
                total_rev * 0.9,
                '按屋顶面积比例分配',
                owners,
                owner_id,
                ratio,
                total_rev * 0.9 * ratio,
                1,
                1,
                NOW()
            );
            
            SET owner_idx = owner_idx + 1;
        END WHILE;
        
        SET period_idx = period_idx + 1;
    END WHILE;
END //
DELIMITER ;

CALL generate_revenue_allocations();
DROP PROCEDURE IF EXISTS generate_revenue_allocations;

-- =============================================
-- 数据初始化完成
-- =============================================
SELECT '数据初始化完成！' AS message;
SELECT COUNT(*) AS sys_user_count FROM sys_user;
SELECT COUNT(*) AS roof_area_count FROM roof_area;
SELECT COUNT(*) AS inverter_count FROM inverter;
SELECT COUNT(*) AS pv_string_count FROM pv_string;
SELECT COUNT(*) AS meter_reading_count FROM meter_reading;
SELECT COUNT(*) AS power_curve_count FROM power_curve;
SELECT COUNT(*) AS vote_topic_count FROM vote_topic;
SELECT COUNT(*) AS vote_record_count FROM vote_record;
SELECT COUNT(*) AS owner_notice_count FROM owner_notice;
SELECT COUNT(*) AS construction_record_count FROM construction_record;
SELECT COUNT(*) AS grid_application_count FROM grid_application;
SELECT COUNT(*) AS revenue_allocation_count FROM revenue_allocation;
SELECT COUNT(*) AS warranty_count FROM warranty;
