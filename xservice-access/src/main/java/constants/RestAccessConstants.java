package constants;

public class RestAccessConstants {

	public static final String SERVICE_NAME = "mc-access";

	public static final String SERVICE_ROOT = "/mc-access";

	public static final int HTTP_PORT = 1100;

	public static final String ONLINE_NUMBER = "/mc-access/online/number.json";

	public static final String DISPATCH = "/mc-access/user/dispatch.json";

	/**
	 * 获取TCP服务器地址 param: userTel 用户手机号
	 * 
	 */
	public static final String SERVER = "/mc-im/server.json";

	/**
	 * 默认获得该订单最新100条消息，如果timestamp存在，则获得timestamp之前100条消息
	 * 
	 * param orderNo 订单号；timestamp
	 */
	public static final String GET_OFFLINE_MESSAGE = "/mc-im/message/offline/get.json";

	/**
	 * param "identity" : 0, //身份标识， 0司机 1乘客 ;userId
	 */
	public static final String get_quick_phrase = "/mc-im/quick/phrase/get.json";

	/**
	 * content
	 */
	public static final String add_quick_phrase = "/mc-im/quick/phrase/add.json";

	/**
	 * id
	 */
	public static final String del_quick_phrase = "/mc-im/quick/phrase/del.json";
}
