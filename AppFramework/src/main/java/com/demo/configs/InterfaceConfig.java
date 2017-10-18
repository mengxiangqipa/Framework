package com.demo.configs;

/**
 * 接口配置
 */

/**
 * @author Administrator
 */
public interface InterfaceConfig
{
	/**
	 * 服务器地址内网
	 */
	//     String BASE_SERVER_URL = "http://192.168.1.99:8080";
	//     String BASE_SERVER_URL = "http://192.168.1.111:8080";
	/**
	 * 服务器地址外网
	 */
	//     String BASE_SERVER_URL = "https://test.haomeng.com";
	String BASE_SERVER_URL = "https://wx.haomeng.com";

	// 注册获取验证码接口
	String getCaptcha_register = "/api/sms/send_reg_code";
	String captcha_register = "/api/sms/send_reg_code";
	// 登录获取验证码接口
	String captcha_login = "/api/sms/dyn_login";
	//申请体检单获取验证码接口
	String captcha_physical = "/api/sms/send_health_code";
	// 注册手机号
	String register = "/api/student/register";
	//学员登录
	String login = "/api/student/login";
	//教练列表的滚动广告
	String banner = "/api/media/index_ad_rotate";
	//服务器返回的普通班3000，vip班5000
	String productType = "/api/product/all_products";
	//活动资讯的滚动广告
	String banner_enquiries = "/api/info/index_ad_rotate";
	//活动资讯的导航列表
	String navigation = "/api/info/nav_list";
	//活动资讯的列表接口
	String infoList = "/api/info/list";
	//教练列表的 恭喜**通过考试的 接口
	String ad_scrollBar = "/api/platform/student_track";
	//学员对教练的评价列表
	String commentList = "/api/teacher/comments";
	//请求教练列表
	String coachList = "/api/area/area_complex";
	//教练列表筛选的地区
	String cityList = "/api/teacher/area_classify_summary";
	//教练列表筛选的班型
	String vehicleTypeList = "/api/product/all_product_name";
	//个人中心
	String personalInfo = "/api/student/my_info";
	//教练详情，我的教练都是它
	String coachDetail = "/api/teacher/teacherDetail";
	//学员确定报名
	String signUp = "/api/student/signupOrUpdateSubject";
	//请求的一些常量类，比如 学车题库 内部的url
	String getConstant = "/api/platform/static_data";
	//个人中心上传头像
	String uploadImage = "/api/student/photoUpload";
	//确定体检单时选择的体检医院
	String physicalAddress = "/api/platform/cq_main_areas";
	//确认体检单
	String ensurePhysicalExamination = "/api/healthBill/finished";
	//确定入籍，通过科一，科二，科三，科四
	String apply = "/api/student/study_flow";
	//确定上车
	String ensureGo = "/api/student/drive_car_submit";
	//评价打赏 教练
	String evaluate = "/api/student/comment";
	//评价打赏 教练时获取 评价标签
	String evaluateLabel = "/api/platform/reward_labels";
	//预约考试
	String make_an_appointment = "/api/student/bespoke_exam";
	String myCoach = "/api/student/my_teacher_info";
	String myCoachHovor = "/api/student/my_choose_info";
	// 忘记密码--获取验证码
	String getCaptchaForgetpwd = "/api/sms/send_pwd_update_code";
	//重置密码
	String resetPWD = "/api/student/reset_pwd";
	//查询报名状态，投诉状态等
	String applyState = "/api/student/my_operates";
	//我的体检单
	String myPhysicalExamination = "/api/healthBill/my_health_bills";
	//申请体检单
	String applyPhysicalExamination = "/api/healthBill/apply";
	//支付order 微信
	String payOrderWchat = "/api/wxPayNew/payOrderByWeixin";
	//支付order 微信
	String payOrderAlipay = "/api/alipay/payByAliPay";
	//体检单可用医院
	String applyPhysicalExaminationAddress = "/api/platform/health_sites";
	//后台返回的投诉标签
	String complaintLabel = "/api/platform/complain_labels";
	//投诉教练
	String complaint = "/api/student/complainTeacher";
	//练车状态提示信息
	String tips = "/api/student/study_flow_tip";
	//版本更新
	String UPDATE = "/api/platform/app_version_update";

	// list
	String list = "controlledList";
	// 闪屏
	String startup = "startup";
	//
	String version = "appUpdate";

	/**
	 * <pre>
	 * 是否允许日志输出。
	 * true 允许Log
	 * false 不允许Log
	 * <pre/>
	 */
	boolean allowLog = true;
	String SUCCESS = "20000";
	String reLogin = "500000";

	String checkUpdateUrl = "http://download.51yyt.com.cn/updates.json";


	String webMainList = "dwr/call/plaincall/SummaryService.queryOrderList.dwr";
	String webBanner = "/gis2/LandmarketAnalys/main.jsp";
	String webMain = "app/new_skin/index.jsp";
	String webSummary = "app/new_skin/summary.jsp";
	String webKPIdetail = "gis2/kpiDetail.jsp?channel_id=465200548";
	String webKPIdetailWithoutId = "gis2/kpiDetail.jsp?channel_id=";
	String jsCLickLogin = "dwr/call/plaincall/SignOnService.login.dwr";
	String webLogin = "";
	//修改昵称
	String modifyNick = "/api/amendNickName";
}
