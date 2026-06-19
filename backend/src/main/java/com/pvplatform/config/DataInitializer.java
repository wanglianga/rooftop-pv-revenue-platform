package com.pvplatform.config;

import com.pvplatform.entity.*;
import com.pvplatform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.Calendar;
import java.util.Date;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private RoofAreaRepository roofAreaRepository;

    @Autowired
    private InverterRepository inverterRepository;

    @Autowired
    private PvStringRepository pvStringRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private PowerCurveRepository powerCurveRepository;

    @Autowired
    private VoteTopicRepository voteTopicRepository;

    @Autowired
    private VoteRecordRepository voteRecordRepository;

    @Autowired
    private OwnerNoticeRepository ownerNoticeRepository;

    @Autowired
    private ConstructionRecordRepository constructionRecordRepository;

    @Autowired
    private GridApplicationRepository gridApplicationRepository;

    @Autowired
    private RevenueAllocationRepository revenueAllocationRepository;

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private GridRectificationRepository gridRectificationRepository;

    @Autowired
    private InverterAnomalyRepository inverterAnomalyRepository;

    @Autowired
    private GenerationForecastRepository generationForecastRepository;

    @Autowired
    private RevenueSettlementRepository revenueSettlementRepository;

    @Autowired
    private GenerationAnomalyRepository generationAnomalyRepository;

    @Autowired
    private SettlementCorrectionRepository settlementCorrectionRepository;

    @Override
    public void run(String... args) {
        if (sysUserRepository.count() == 0) {
            initSysUsers();
        }
        if (roofAreaRepository.count() == 0) {
            initRoofAreas();
        }
        if (inverterRepository.count() == 0) {
            initInverters();
        }
        if (pvStringRepository.count() == 0) {
            initPvStrings();
        }
        if (meterReadingRepository.count() == 0) {
            initMeterReadings();
        }
        if (powerCurveRepository.count() == 0) {
            initPowerCurves();
        }
        if (voteTopicRepository.count() == 0) {
            initVoteTopics();
        }
        if (voteRecordRepository.count() == 0) {
            initVoteRecords();
        }
        if (ownerNoticeRepository.count() == 0) {
            initOwnerNotices();
        }
        if (constructionRecordRepository.count() == 0) {
            initConstructionRecords();
        }
        if (gridApplicationRepository.count() == 0) {
            initGridApplications();
        }
        if (revenueAllocationRepository.count() == 0) {
            initRevenueAllocations();
        }
        if (warrantyRepository.count() == 0) {
            initWarranties();
        }
        if (gridRectificationRepository.count() == 0) {
            initGridRectifications();
        }
        if (inverterAnomalyRepository.count() == 0) {
            initInverterAnomalies();
        }
        if (generationForecastRepository.count() == 0) {
            initGenerationForecasts();
        }
        if (revenueSettlementRepository.count() == 0) {
            initRevenueSettlements();
        }
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    private Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    private void initSysUsers() {
        Date now = new Date();

        SysUser admin = new SysUser();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setRealName("系统管理员");
        admin.setPhone("13800138000");
        admin.setRole("ADMIN");
        admin.setStatus(1);
        admin.setCreateTime(now);
        sysUserRepository.save(admin);

        SysUser owner1 = new SysUser();
        owner1.setUsername("owner1");
        owner1.setPassword("123456");
        owner1.setRealName("张三");
        owner1.setPhone("13800138001");
        owner1.setRole("OWNER");
        owner1.setStatus(1);
        owner1.setCreateTime(now);
        sysUserRepository.save(owner1);

        SysUser owner2 = new SysUser();
        owner2.setUsername("owner2");
        owner2.setPassword("123456");
        owner2.setRealName("李四");
        owner2.setPhone("13800138002");
        owner2.setRole("OWNER");
        owner2.setStatus(1);
        owner2.setCreateTime(now);
        sysUserRepository.save(owner2);

        SysUser owner3 = new SysUser();
        owner3.setUsername("owner3");
        owner3.setPassword("123456");
        owner3.setRealName("王五");
        owner3.setPhone("13800138003");
        owner3.setRole("OWNER");
        owner3.setStatus(1);
        owner3.setCreateTime(now);
        sysUserRepository.save(owner3);
    }

    private void initRoofAreas() {
        Date now = new Date();

        String[] buildings = {"1号楼", "2号楼", "3号楼", "4号楼", "5号楼"};
        String[] areaNames = {"东单元屋顶", "西单元屋顶", "整体屋顶", "A区屋顶", "B区屋顶"};
        double[] sizes = {120.5, 150.0, 200.3, 180.8, 95.6};
        String[] ownerTypes = {"公共区域", "业主共有", "公共区域", "业主共有", "公共区域"};

        for (int i = 0; i < buildings.length; i++) {
            RoofArea area = new RoofArea();
            area.setBuildingNo(buildings[i]);
            area.setAreaName(areaNames[i]);
            area.setAreaSize(sizes[i]);
            area.setOwnerType(ownerTypes[i]);
            area.setUsable(1);
            area.setDescription(buildings[i] + areaNames[i] + "，可安装光伏组件");
            area.setCreateBy(1L);
            area.setCreateTime(now);
            roofAreaRepository.save(area);
        }
    }

    private void initInverters() {
        Date now = new Date();
        Date installDate = addDays(now, -90);

        String[] codes = {"INV-001", "INV-002", "INV-003", "INV-004", "INV-005"};
        String[] models = {"SUN-20K", "SUN-30K", "SUN-25K", "SUN-40K", "SUN-15K"};
        double[] capacities = {20.0, 30.0, 25.0, 40.0, 15.0};

        for (int i = 0; i < codes.length; i++) {
            Inverter inv = new Inverter();
            inv.setRoofAreaId((long) (i + 1));
            inv.setInverterCode(codes[i]);
            inv.setModel(models[i]);
            inv.setCapacity(capacities[i]);
            inv.setInstallDate(installDate);
            inv.setStatus(1);
            inv.setCreateBy(1L);
            inv.setCreateTime(now);
            inverterRepository.save(inv);
        }
    }

    private void initPvStrings() {
        Date now = new Date();
        Date installDate = addDays(now, -90);

        String[] codes = {"STR-001", "STR-002", "STR-003", "STR-004", "STR-005", "STR-006", "STR-007", "STR-008", "STR-009", "STR-010"};
        int[] panels = {20, 22, 18, 24, 20, 22, 18, 24, 20, 22};
        String[] models = {"JKM395N", "JKM395N", "JKM405N", "JKM405N", "JKM395N", "JKM395N", "JKM405N", "JKM405N", "JKM395N", "JKM395N"};

        for (int i = 0; i < codes.length; i++) {
            PvString str = new PvString();
            str.setRoofAreaId((long) ((i % 5) + 1));
            str.setStringCode(codes[i]);
            str.setPanelCount(panels[i]);
            str.setPanelModel(models[i]);
            str.setInstallDate(installDate);
            str.setStatus(1);
            str.setCreateBy(1L);
            str.setCreateTime(now);
            pvStringRepository.save(str);
        }
    }

    private void initMeterReadings() {
        Date now = new Date();

        for (int invId = 1; invId <= 5; invId++) {
            for (int day = 0; day < 30; day++) {
                Date readingDate = addDays(now, -day);
                MeterReading mr = new MeterReading();
                mr.setInverterId((long) invId);
                mr.setReadingDate(readingDate);
                mr.setGeneration(100.0 + Math.random() * 50);
                mr.setMeterReading(1000.0 + day * (100.0 + Math.random() * 50));
                mr.setReadingBy(1L);
                mr.setCreateTime(now);
                meterReadingRepository.save(mr);
            }
        }
    }

    private void initPowerCurves() {
        Date now = new Date();
        Date baseDate = addDays(now, -1);

        for (int invId = 1; invId <= 5; invId++) {
            for (int hour = 6; hour <= 18; hour++) {
                Date recordTime = addHours(baseDate, hour);
                PowerCurve pc = new PowerCurve();
                pc.setInverterId((long) invId);
                pc.setRecordTime(recordTime);
                double powerFactor = Math.sin(Math.PI * (hour - 6) / 12);
                pc.setPower(5.0 * powerFactor + Math.random() * 0.5);
                pc.setVoltage(220.0 + Math.random() * 10);
                pc.setCurrent(20.0 * powerFactor + Math.random() * 2);
                pc.setTemperature(25.0 + powerFactor * 20 + Math.random() * 5);
                pc.setCreateTime(now);
                powerCurveRepository.save(pc);
            }
        }
    }

    private void initVoteTopics() {
        Date now = new Date();

        VoteTopic topic1 = new VoteTopic();
        topic1.setTitle("关于安装屋顶光伏系统的提案");
        topic1.setContent("为充分利用小区屋顶资源，降低业主用电成本，拟在小区公共屋顶安装光伏发电系统。预计装机容量100KW，投资约50万元，预计年发电量约12万度，投资回收期约6-8年。");
        topic1.setVoteRule("双三分之二原则：需经参与表决专有部分面积四分之三以上的业主且参与表决人数四分之三以上的业主同意");
        topic1.setStatus(1);
        topic1.setStartDate(addDays(now, -7));
        topic1.setEndDate(addDays(now, 7));
        topic1.setCreateBy(1L);
        topic1.setCreateTime(now);
        voteTopicRepository.save(topic1);

        VoteTopic topic2 = new VoteTopic();
        topic2.setTitle("关于光伏系统维护方案的投票");
        topic2.setContent("为确保光伏系统长期稳定运行，拟委托专业公司进行年度维护，维护费用约5000元/年，从公共收益中支出。");
        topic2.setVoteRule("简单多数原则：参与表决人数过半且同意人数过半即通过");
        topic2.setStatus(0);
        topic2.setStartDate(addDays(now, 7));
        topic2.setEndDate(addDays(now, 21));
        topic2.setCreateBy(1L);
        topic2.setCreateTime(now);
        voteTopicRepository.save(topic2);

        VoteTopic topic3 = new VoteTopic();
        topic3.setTitle("关于光伏收益分配方式的调整");
        topic3.setContent("鉴于部分业主屋顶面积差异较大，拟调整收益分配方式，从按户平均分配调整为按屋顶面积比例分配。");
        topic3.setVoteRule("双三分之二原则：需经参与表决专有部分面积四分之三以上的业主且参与表决人数四分之三以上的业主同意");
        topic3.setStatus(2);
        topic3.setStartDate(addDays(now, -30));
        topic3.setEndDate(addDays(now, -16));
        topic3.setCreateBy(1L);
        topic3.setCreateTime(now);
        voteTopicRepository.save(topic3);
    }

    private void initVoteRecords() {
        Date now = new Date();

        String[] results = {"同意", "同意", "同意", "同意", "同意", "反对", "弃权", "同意", "同意", "同意"};

        for (int i = 0; i < 10; i++) {
            VoteRecord vr = new VoteRecord();
            vr.setTopicId(1L);
            vr.setOwnerId((long) ((i % 3) + 2));
            vr.setVoteResult(results[i]);
            vr.setVoteTime(addDays(now, -5 + (i % 3)));
            vr.setRemark(results[i].equals("同意") ? "支持绿色能源" : (results[i].equals("反对") ? "担心安全问题" : ""));
            voteRecordRepository.save(vr);
        }
    }

    private void initOwnerNotices() {
        Date now = new Date();

        OwnerNotice notice1 = new OwnerNotice();
        notice1.setTitle("关于屋顶光伏系统施工的通知");
        notice1.setContent("各位业主：小区公共屋顶光伏系统将于2024年1月15日开始施工，预计工期30天。施工期间请注意安全，禁止进入施工区域。如有疑问请联系物业。");
        notice1.setNoticeType("施工通知");
        notice1.setTargetAudience("全体业主");
        notice1.setPublishTime(addDays(now, -10));
        notice1.setCreateBy(1L);
        notice1.setCreateTime(now);
        ownerNoticeRepository.save(notice1);

        OwnerNotice notice2 = new OwnerNotice();
        notice2.setTitle("光伏系统并网申请已受理");
        notice2.setContent("各位业主：小区光伏系统并网申请已获供电局受理，预计2024年2月完成并网。并网后将按照国家相关政策享受上网电价补贴。");
        notice2.setNoticeType("重要通知");
        notice2.setTargetAudience("全体业主");
        notice2.setPublishTime(addDays(now, -5));
        notice2.setCreateBy(1L);
        notice2.setCreateTime(now);
        ownerNoticeRepository.save(notice2);

        OwnerNotice notice3 = new OwnerNotice();
        notice3.setTitle("2024年1月发电量统计");
        notice3.setContent("各位业主：2024年1月光伏发电总量为12500度，其中自用8500度，上网4000度。本月收益约850元，将纳入业主公共收益账户。");
        notice3.setNoticeType("月度报告");
        notice3.setTargetAudience("全体业主");
        notice3.setPublishTime(addDays(now, -1));
        notice3.setCreateBy(1L);
        notice3.setCreateTime(now);
        ownerNoticeRepository.save(notice3);
    }

    private void initConstructionRecords() {
        Date now = new Date();

        String[] stages = {"勘察设计", "基础施工", "支架安装", "组件安装", "电气安装", "系统调试", "竣工验收"};
        String[] contents = {
            "完成屋顶现场勘察，确定安装方案",
            "完成混凝土基础浇筑，养护7天",
            "完成光伏支架安装，检查稳固性",
            "完成光伏组件安装，共200块",
            "完成逆变器、电表等电气设备安装接线",
            "系统通电调试，各项参数正常",
            "通过供电局验收，具备并网条件"
        };
        String[] operators = {"王工程师", "李施工队", "张安装队", "张安装队", "赵电工", "刘技术员", "供电局"};

        for (int i = 0; i < stages.length; i++) {
            ConstructionRecord cr = new ConstructionRecord();
            cr.setRoofAreaId(1L);
            cr.setStage(stages[i]);
            cr.setRecordContent(contents[i]);
            cr.setOperator(operators[i]);
            cr.setOperationTime(addDays(now, -60 + i * 8));
            cr.setImages("");
            cr.setRemark("");
            cr.setCreateBy(1L);
            cr.setCreateTime(now);
            constructionRecordRepository.save(cr);
        }
    }

    private void initGridApplications() {
        Date now = new Date();

        GridApplication app1 = new GridApplication();
        app1.setRoofAreaId(1L);
        app1.setApplicant("业主委员会");
        app1.setApplicationDate(addDays(now, -30));
        app1.setStatus("已通过");
        app1.setGridNo("GD-2024-00123");
        app1.setAcceptanceOpinion("申请材料齐全，符合并网条件，同意并网");
        app1.setFeedbackBy(1L);
        app1.setFeedbackTime(addDays(now, -15));
        app1.setCreateBy(1L);
        app1.setCreateTime(now);
        gridApplicationRepository.save(app1);

        GridApplication app2 = new GridApplication();
        app2.setRoofAreaId(2L);
        app2.setApplicant("业主委员会");
        app2.setApplicationDate(addDays(now, -20));
        app2.setStatus("审核中");
        app2.setGridNo("");
        app2.setAcceptanceOpinion("");
        app2.setFeedbackBy(null);
        app2.setFeedbackTime(null);
        app2.setCreateBy(1L);
        app2.setCreateTime(now);
        gridApplicationRepository.save(app2);
    }

    private void initRevenueAllocations() {
        Date now = new Date();

        String[] periods = {"2023-12", "2024-01"};
        String[] buildings = {"1号楼", "2号楼", "3号楼"};
        String[] owners = {"张三", "李四", "王五"};
        long[] ownerIds = {2L, 3L, 4L};
        double[] ratios = {0.35, 0.35, 0.30};

        for (String period : periods) {
            double totalRevenue = 1500.0 + Math.random() * 500;
            for (int i = 0; i < buildings.length; i++) {
                RevenueAllocation ra = new RevenueAllocation();
                ra.setPeriod(period);
                ra.setBuildingNo(buildings[i]);
                ra.setTotalRevenue(totalRevenue);
                ra.setAllocatedAmount(totalRevenue * 0.9);
                ra.setAllocationRule("按屋顶面积比例分配");
                ra.setOwnerName(owners[i]);
                ra.setOwnerId(ownerIds[i]);
                ra.setAreaRatio(ratios[i]);
                ra.setAmount(totalRevenue * 0.9 * ratios[i]);
                ra.setStatus(1);
                ra.setCreateBy(1L);
                ra.setCreateTime(now);
                revenueAllocationRepository.save(ra);
            }
        }
    }

    private void initWarranties() {
        Date now = new Date();

        Warranty w1 = new Warranty();
        w1.setDeviceType("INVERTER");
        w1.setDeviceId(1L);
        w1.setDeviceCode("INV-001");
        w1.setWarrantyStart(addDays(now, -90));
        w1.setWarrantyEnd(addDays(now, 365 * 5));
        w1.setWarrantyContent("整机5年质保，免费维修更换");
        w1.setSupplier("阳光电源股份有限公司");
        w1.setContactPhone("400-888-8888");
        w1.setCreateBy(1L);
        w1.setCreateTime(now);
        warrantyRepository.save(w1);

        Warranty w2 = new Warranty();
        w2.setDeviceType("PV_PANEL");
        w2.setDeviceId(1L);
        w2.setDeviceCode("STR-001");
        w2.setWarrantyStart(addDays(now, -90));
        w2.setWarrantyEnd(addDays(now, 365 * 25));
        w2.setWarrantyContent("产品材料和工艺质保10年，功率质保25年");
        w2.setSupplier("晶科能源股份有限公司");
        w2.setContactPhone("400-666-6666");
        w2.setCreateBy(1L);
        w2.setCreateTime(now);
        warrantyRepository.save(w2);

        Warranty w3 = new Warranty();
        w3.setDeviceType("INVERTER");
        w3.setDeviceId(2L);
        w3.setDeviceCode("INV-002");
        w3.setWarrantyStart(addDays(now, -90));
        w3.setWarrantyEnd(addDays(now, 365 * 5));
        w3.setWarrantyContent("整机5年质保，免费维修更换");
        w3.setSupplier("阳光电源股份有限公司");
        w3.setContactPhone("400-888-8888");
        w3.setCreateBy(1L);
        w3.setCreateTime(now);
        warrantyRepository.save(w3);
    }

    private void initGridRectifications() {
        Date now = new Date();

        GridRectification r1 = new GridRectification();
        r1.setGridApplicationId(2L);
        r1.setRectificationType("接线图补充");
        r1.setDescription("补充3号楼屋顶光伏接线图，更新电气系统单线图");
        r1.setPhotos("");
        r1.setFileVersion("V2.1");
        r1.setRetestResult("复测通过，接线图与现场一致");
        r1.setSubmitter("李施工队");
        r1.setSubmitTime(addDays(now, -5));
        r1.setStatus("APPROVED");
        r1.setCreateBy(1L);
        r1.setCreateTime(now);
        gridRectificationRepository.save(r1);

        GridRectification r2 = new GridRectification();
        r2.setGridApplicationId(2L);
        r2.setRectificationType("保护装置完善");
        r2.setDescription("增加防孤岛保护装置，完善继电保护方案");
        r2.setPhotos("");
        r2.setFileVersion("V3.0");
        r2.setRetestResult("保护装置动作测试正常");
        r2.setSubmitter("赵电工");
        r2.setSubmitTime(addDays(now, -3));
        r2.setStatus("SUBMITTED");
        r2.setCreateBy(1L);
        r2.setCreateTime(now);
        gridRectificationRepository.save(r2);
    }

    private void initInverterAnomalies() {
        Date now = new Date();

        InverterAnomaly a1 = new InverterAnomaly();
        a1.setInverterId(3L);
        a1.setAnomalyType("OFFLINE");
        a1.setDescription("INV-003逆变器通讯中断，疑似通讯模块故障");
        a1.setDiscoveryTime(addDays(now, -10));
        a1.setAffectedPvStrings("STR-003,STR-008");
        a1.setAffectedBuilding("3号楼");
        a1.setDowntimeHours(48.0);
        a1.setTroubleshootProcess("1.检查通讯线缆连接 2.更换通讯模块 3.重新配置IP地址 4.测试通讯恢复正常");
        a1.setRepairTime(addDays(now, -8));
        a1.setRepairResult("更换通讯模块后恢复正常，已连续运行48小时无异常");
        a1.setAffectedDates(addDays(now, -10) + "至" + addDays(now, -8));
        a1.setBeforeCurveDate("2026-06-05");
        a1.setAfterCurveDate("2026-06-09");
        a1.setCurveComparisonResult("修复后日平均功率恢复至故障前95%以上，发电曲线正常");
        a1.setStatus("RESOLVED");
        a1.setOperator("刘运维");
        a1.setCreateBy(1L);
        a1.setCreateTime(now);
        inverterAnomalyRepository.save(a1);

        InverterAnomaly a2 = new InverterAnomaly();
        a2.setInverterId(1L);
        a2.setAnomalyType("POWER_DROP");
        a2.setDescription("INV-001发电量突降30%，疑似组件遮挡或MPPT异常");
        a2.setDiscoveryTime(addDays(now, -2));
        a2.setAffectedPvStrings("STR-001,STR-006");
        a2.setAffectedBuilding("1号楼");
        a2.setDowntimeHours(0.0);
        a2.setTroubleshootProcess("1.现场检查发现1号楼东侧新搭建脚手架造成遮挡 2.协调物业拆除脚手架 3.检查MPPT跟踪状态");
        a2.setAffectedDates(addDays(now, -2) + "至今");
        a2.setStatus("OPEN");
        a2.setOperator("王运维");
        a2.setCreateBy(1L);
        a2.setCreateTime(now);
        inverterAnomalyRepository.save(a2);

        InverterAnomaly a3 = new InverterAnomaly();
        a3.setInverterId(5L);
        a3.setAnomalyType("SHADING_INCREASE");
        a3.setDescription("INV-005所在区域遮挡明显增多，发电效率下降");
        a3.setDiscoveryTime(addDays(now, -5));
        a3.setAffectedPvStrings("STR-005,STR-010");
        a3.setAffectedBuilding("5号楼");
        a3.setDowntimeHours(0.0);
        a3.setTroubleshootProcess("1.检查5号楼B区屋顶周边树木生长情况 2.确认树木遮挡影响范围 3.已通知物业进行修剪");
        a3.setAffectedDates(addDays(now, -5) + "至今");
        a3.setStatus("IN_PROGRESS");
        a3.setOperator("陈运维");
        a3.setCreateBy(1L);
        a3.setCreateTime(now);
        inverterAnomalyRepository.save(a3);
    }

    private void initGenerationForecasts() {
        Date now = new Date();
        String[] weatherTypes = {"SUNNY", "CLOUDY", "RAINY"};
        double[] weatherFactors = {1.0, 0.65, 0.25};
        double annualDegradation = 0.008;
        double degradationFactor = Math.pow(1 - annualDegradation, 90.0 / 365.0);

        double[] capacities = {20.0, 30.0, 25.0, 40.0, 15.0};

        for (int invId = 1; invId <= 5; invId++) {
            double baseDailyGen = capacities[invId - 1] * 4.5;

            for (int day = 0; day < 7; day++) {
                Date forecastDate = addDays(now, day + 1);
                int weatherIdx = (day + invId) % 3;
                String weatherType = weatherTypes[weatherIdx];
                double weatherFactor = weatherFactors[weatherIdx];

                double randomFactor = 0.9 + Math.random() * 0.2;
                double forecastGen = Math.round(baseDailyGen * weatherFactor * degradationFactor * randomFactor * 100.0) / 100.0;

                GenerationForecast forecast = new GenerationForecast();
                forecast.setInverterId((long) invId);
                forecast.setForecastDate(forecastDate);
                forecast.setForecastGeneration(forecastGen);
                forecast.setWeatherType(weatherType);
                forecast.setWeatherTempHigh(25.0 + Math.random() * 10);
                forecast.setWeatherTempLow(15.0 + Math.random() * 5);
                forecast.setDegradationFactor(Math.round(degradationFactor * 10000.0) / 10000.0);
                forecast.setHistoricalReference("历史同期日均发电量约" + String.format("%.2f", baseDailyGen) + "kWh");
                forecast.setIsAnomaly(0);
                forecast.setCreateTime(now);
                forecast.setUpdateTime(now);
                generationForecastRepository.save(forecast);
            }

            for (int day = 1; day <= 3; day++) {
                Date forecastDate = addDays(now, -day);
                int weatherIdx = (day + invId + 1) % 3;
                String weatherType = weatherTypes[weatherIdx];
                double weatherFactor = weatherFactors[weatherIdx];

                double baseForecast = baseDailyGen * weatherFactor * degradationFactor;
                double forecastGen = Math.round(baseForecast * 100.0) / 100.0;
                double actualGen;

                if (day == 1 && invId == 2) {
                    actualGen = Math.round(forecastGen * 0.7 * 100.0) / 100.0;
                } else {
                    actualGen = Math.round(forecastGen * (0.95 + Math.random() * 0.1) * 100.0) / 100.0;
                }

                double deviation = forecastGen > 0 ? Math.abs(actualGen - forecastGen) / forecastGen : 0;

                GenerationForecast forecast = new GenerationForecast();
                forecast.setInverterId((long) invId);
                forecast.setForecastDate(forecastDate);
                forecast.setForecastGeneration(forecastGen);
                forecast.setActualGeneration(actualGen);
                forecast.setWeatherType(weatherType);
                forecast.setWeatherTempHigh(25.0 + Math.random() * 10);
                forecast.setWeatherTempLow(15.0 + Math.random() * 5);
                forecast.setDegradationFactor(Math.round(degradationFactor * 10000.0) / 10000.0);
                forecast.setHistoricalReference("历史同期日均发电量约" + String.format("%.2f", baseDailyGen) + "kWh");
                forecast.setDeviationRate(Math.round(deviation * 10000.0) / 100.0);
                forecast.setIsAnomaly(deviation > 0.15 ? 1 : 0);
                forecast.setCreateTime(now);
                forecast.setUpdateTime(now);
                generationForecastRepository.save(forecast);
            }
        }
    }

    private void initRevenueSettlements() {
        Date now = new Date();

        RevenueSettlement s1 = new RevenueSettlement();
        s1.setSettlementNo("ST" + new java.text.SimpleDateFormat("yyyyMMdd").format(now) + "001");
        s1.setStartDate(now);
        s1.setEndDate(addDays(now, 7));
        s1.setTotalForecastGeneration(4580.50);
        s1.setTotalActualGeneration(0.0);
        s1.setUnitPrice(0.512);
        s1.setSelfUseRatio(0.68);
        s1.setGridRatio(0.32);
        s1.setSelfUsePrice(0.56);
        s1.setGridPrice(0.41);
        s1.setForecastRevenue(2345.80);
        s1.setActualRevenue(0.0);
        s1.setDeviationAmount(0.0);
        s1.setDeviationRate(0.0);
        s1.setStatus("DRAFT");
        s1.setWeatherSummary("晴天、多云");
        s1.setForecastBasis("1. 基于历史发电曲线数据，参考同期日均发电量；2. 未来天气预测：晴天、多云；3. 组件衰减系数：0.8%/年；4. 电价标准：自用0.56元/度，上网0.41元/度");
        s1.setCreateBy(1L);
        s1.setCreateTime(now);
        s1.setUpdateTime(now);
        revenueSettlementRepository.save(s1);

        RevenueSettlement s2 = new RevenueSettlement();
        s2.setSettlementNo("ST" + new java.text.SimpleDateFormat("yyyyMMdd").format(addDays(now, -3)) + "001");
        s2.setStartDate(addDays(now, -10));
        s2.setEndDate(addDays(now, -3));
        s2.setTotalForecastGeneration(4320.80);
        s2.setTotalActualGeneration(4150.30);
        s2.setUnitPrice(0.512);
        s2.setSelfUseRatio(0.68);
        s2.setGridRatio(0.32);
        s2.setSelfUsePrice(0.56);
        s2.setGridPrice(0.41);
        s2.setForecastRevenue(2211.80);
        s2.setActualRevenue(2124.95);
        s2.setDeviationAmount(-86.85);
        s2.setDeviationRate(-3.93);
        s2.setStatus("REVIEWED");
        s2.setWeatherSummary("晴天、多云、雨天");
        s2.setForecastBasis("1. 基于历史发电曲线数据，参考同期日均发电量；2. 未来天气预测：晴天、多云、雨天；3. 组件衰减系数：0.8%/年；4. 电价标准：自用0.56元/度，上网0.41元/度");
        s2.setRemark("已审核，收益偏差在正常范围内");
        s2.setReviewedBy(1L);
        s2.setReviewTime(now);
        s2.setCreateBy(1L);
        s2.setCreateTime(addDays(now, -10));
        s2.setUpdateTime(now);
        revenueSettlementRepository.save(s2);

        GenerationAnomaly anomaly = new GenerationAnomaly();
        anomaly.setAnomalyNo("GA" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(now) + "2");
        anomaly.setInverterId(2L);
        anomaly.setAnomalyDate(addDays(now, -1));
        anomaly.setForecastGeneration(135.60);
        anomaly.setActualGeneration(94.92);
        anomaly.setDeviationRate(30.0);
        anomaly.setDeviationAmount(-40.68);
        anomaly.setStatus("PENDING");
        anomaly.setCreateBy(1L);
        anomaly.setCreateTime(now);
        anomaly.setUpdateTime(now);
        generationAnomalyRepository.save(anomaly);

        GenerationAnomaly anomaly2 = new GenerationAnomaly();
        anomaly2.setAnomalyNo("GA" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(addDays(now, -2)) + "5");
        anomaly2.setInverterId(5L);
        anomaly2.setAnomalyDate(addDays(now, -2));
        anomaly2.setForecastGeneration(72.50);
        anomaly2.setActualGeneration(55.10);
        anomaly2.setDeviationRate(24.0);
        anomaly2.setDeviationAmount(-17.40);
        anomaly2.setStatus("RESOLVED");
        anomaly2.setDeviationReasonCategory("WEATHER_CHANGE");
        anomaly2.setDeviationReason("天气突变");
        anomaly2.setDetailDescription("当日突发雷阵雨天气，实际日照时长严重不足，导致发电量大幅低于预期");
        anomaly2.setHandledBy(1L);
        anomaly2.setHandleTime(addDays(now, -1));
        anomaly2.setCreateBy(1L);
        anomaly2.setCreateTime(addDays(now, -2));
        anomaly2.setUpdateTime(addDays(now, -1));
        generationAnomalyRepository.save(anomaly2);
    }
}
