package constant;

/**
 * 消息推送常量
 */
public class PushConsts {
	
	public final static String AD_PASSENGER_MSG_PREFIX = "AD_PASSENGER_MSG";
	
	public final static String apnsToken = "apnsToken";
	
	public final static String PUSH_CHANNEL_VERTICLE_PREFIX = "PUSH_CHANNEL_VERTICLE_PREFIX_";
	//小米推送:通知栏
	public static final int XIAOMI_PASS_THROUGH_TONGZHILAN = 0;
	//小米推送：透传
	public static final int XIAOMI_PASS_THROUGH_TOUCHUAN = 1;
	//小米推送声音：默认
	public static final int XIAOMI_NOTIFY_TYPE_DEFAULT_SOUND = 1;

	// ---------------------------------------- SOCKET推送相关的常量字段 ----------------------------------------

	// 用户消息队列key（乘客）
	public static final String _MSG_LIST_PASSENGER = "MSGLIST_PASSENGER";

	//SOCKET推送，下游报文体中的method
	public static final String SOCKET_SEND_METHOD = "sendmsg";

	//SOCKET推送，params中的一个字段默认值用到
	public static final int ZERO = 0;


	// ---------------------------------------------- 上报相关常量 ---------------------------------------------

	//AppCode 乘客端1001, 司机端1002
	public static final Integer MsgStat_APPCODE_ENGER = 1001;

	//系统类型， 安卓端:1; ISO:2;
	public static final Integer MsgStat_OSTYPE_ANDROID = 1;

	//上报类型 1发送，2接收
	public static final Integer MsgStat_ACTION_SEND = 1;
	
	//推送跳转标识:我的账户
	public static final int JUMP_FLAG_MY_ACCOUNT=0;
	
	//推送跳转标识：我的行程页
	public static final int JUMP_FLAG_MY_JOURNEY=1;
	
	//推送跳转标识：充值页
	public static final int JUMP_FLAG_CHARGE_PAGE=2;
	
	//推送跳转标识：优惠券页
	public static final int JUMP_FLAG_COUPON_PAGE=3;
	
	//推送跳转标识：信用卡页
	public static final int JUMP_FLAG_CREDIT_PAGE=4;
}
