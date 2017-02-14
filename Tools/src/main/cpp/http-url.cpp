#include <jni.h>

extern "C" {
	/**
	 * 总地址
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getOnlineHttpUrl(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("https://naucu.com:8443/englishStudy/");
	}
	/**
	 * 测试地址
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getOfflineHttpUrl(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("http://192.168.31.6:8080/englishStudy/");
	}
	/**
	 * 修改手机号验证码
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_modifyPhonenumber(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("updateOldTel.html");
	}
	/**
	 * 登录接口名
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getLogin(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("userLogin");
	}
	/**
	 * 注册验证码获取
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getRegisterGetCode(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("userRegisterGetCode");
	}
	/**
	 * 注册
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getRegister(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("userRegister");
	}
	/**
	 * 修改密码验证码
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_modifyPassword(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("wjPassword");
	}
	/**
	 * 修改密码
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getchangePassword(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("userchangePassword");
	}
	/**
	 *添加头像
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_addHeadPortrait(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("addGeRenTouXiang");
	}
	/**
	 *添加昵称
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_addNickname(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("addGeRenUserName");
	}
	/**
	 *添加性别
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_addGender(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("addGeRenGender");
	}
	/**
	 *获取用户全部信息
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getUser(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("getUserAfterAdd");
	}
	/**
	 *获取首页展示电子书
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getALLbook(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("selectALLbook");
	}
	/**
	 *获取二级电子书
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getALLbookSecondary(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryBookAll");
	}
	/**
	 *获取分类
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getDirectoryAccess(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("querybookTwoCategory");
	}
	/**
	 *获取欢迎图片
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getWelcome(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("beginDongHua");
	}
	/**
	 *电子书查询
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getQueryBookby(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryBookbyName");
	}
	/**
	 *电子书等级查询
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getQueryBookbyLevel(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryBookbyLevel");
	}
	/**
	 *电子书下载
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getQueryDownload(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryBookUrl");
	}

	/**
	 * 电影接口
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getMovie(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("selectALLMovie");
	}
	/**
	 * 电影子类别
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getMovieBytwoCategory(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryMovieBytwoCategory");
	}
	/**
	 * 电影搜索
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getqueryMoviesbysearch(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("queryMoviesbyName");
	}
	/**
	 * 电影字幕下载
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getMoviesSubtitles(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("selecMovieUrl");
	}
	/**
	 * 支付宝
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getPaytreasure(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("aliPayOrderBuying");
	}
	/**
	 * 支付宝支付状态回调
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getPayreturn(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("AliPayBuyed");
	}

	/**
	 * 支付金额获取
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_selectPrice(JNIEnv *env,
				jobject job) {
			return env->NewStringUTF("selectPrice");
		}

	/**
	 * 微信支付
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getweiXinPay(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("weiXinPayOrderBuying");
	}

	/**
	 * 微信支付状态回调
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getweiXinPayReturn(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("weixin");
	}

	/**
	 * 美剧接口
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getTVShow(JNIEnv *env,
			jobject job) {
		return env->NewStringUTF("selectALLTelev");
	}
	/**
	 * 翻译接口
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getBaiduTranslation(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("translateZiFuChun");
	}
	/**
	 * 美剧二级
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getTvShowTelevBy(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("selectTelevBytwoCategory");
	}
	/**
	 * 美剧二级下载
	 */
	jstring Java_com_lijunsai_httpInterface_HttpInterface_getTvShowTelevFlie(
			JNIEnv *env, jobject job) {
		return env->NewStringUTF("selectTelevFliebyId");
	}
}
