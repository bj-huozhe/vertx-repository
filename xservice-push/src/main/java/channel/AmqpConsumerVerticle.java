package channel;

import constant.ConnectionConsts;
import constant.PushConsts;
import domain.AmqpConsumeMessage;
import enums.MsgStatusEnum;
import enums.PushTypeEnum;
import io.netty.util.internal.StringUtil;
import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import service.*;
import util.MsgUtil;
import util.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 
 * @author yanglf
 * 
 *         消息推送消费入口类
 *
 */

public class AmqpConsumerVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(AmqpConsumerVerticle.class);

	private Object activity = null;

	private SocketPushService socketPushService;

	private XiaoMiPushService xiaomiPushService;

	private GcmPushService gcmPushService;

	private RedisService redisService;

	private MsgRecordService deviceService;

	//接收到的消息
	private JsonObject receiveMsg = null;

	//上游消息id
	private String msgId;
	//小米，GCM   token
	private String token;
	//推送类型
	private String sendType;
	@Override
	public void start() throws Exception {

		this.initService();
		//接收消息
		this.recivedMessage();

	}

	private void initService() {

		socketPushService = SocketPushService.createProxy(vertx);

		xiaomiPushService = XiaoMiPushService.createLocalProxy(vertx);

		gcmPushService = GcmPushService.createLocalProxy(vertx);

		deviceService = MsgRecordService.createLocalProxy(vertx);

		redisService = RedisService.createLocalProxy(vertx);

	}

	private void consumMsg() {
		// 校验
		if (!validateRecieveMsg(receiveMsg)) {
			return;
		}
		msgId =  (String) receiveMsg.getValue("msgId");
		token = (String) receiveMsg.getValue("deviceToken"); // sokit、gcm,小米连接token
		String devicePushType = (String) receiveMsg.getValue("devicePushType"); // 消息推送类型
		sendType = MsgUtil.convertCode(devicePushType);

		Object customerId = receiveMsg.getValue("customerId");
		String apnsToken = (String) receiveMsg.getValue("apnsToken");

		redisService.get(PushConsts.AD_PASSENGER_MSG_PREFIX + msgId + "_" + customerId, res -> {
			if (res.succeeded()) {
				activity = res.result();
			}else{
				logger.error("from redis get key fail : key = " + PushConsts.AD_PASSENGER_MSG_PREFIX + msgId + "_" + customerId);
			}
		});
		if (activity != null) {
			logger.error("消费PassengerMsg：这个消息发送过，禁止重复发送！，msgId==:" + msgId);
			return;
		}
		if (StringUtil.isNullOrEmpty(apnsToken) || "null".equals(apnsToken)) {
			// 推送消息到下游
			pushMsgToDownStream();
			// 消息入库
			saveMsgRecord();
		}
	}

	/**
	 * 保存消息记录
	 */
	private void saveMsgRecord() {
		AmqpConsumeMessage msg=new AmqpConsumeMessage();
		msg.setAmqpMsgId(msgId);
		msg.setChannel(sendType);
		msg.setMsgBody(receiveMsg.toString());
		msg.setStatus(MsgStatusEnum.SUCCESS.getCode());
		deviceService.addMessage(msg, res ->{
			if(res.succeeded()){
				logger.info("保存消息成功：" + res);
			}else{
				logger.info("保存消息失败：" + res.cause());
			}
		});
	}

	private void pushMsgToDownStream() {
		receiveMsg.put("regId", token);
		if (PushTypeEnum.SOCKET.getCode().equals(sendType)) {
			// socket推送
			logger.info("开始走socket推送");
			socketPushService.sendMsg(receiveMsg.toString(), res->{
				if(res.succeeded()){
					logger.info("socket推送成功");
				}else{
					logger.error("socket推送失败");
				}
			});
		} else if (PushTypeEnum.GCM.getCode().equals(sendType)) {
			// gcm推送
			logger.info("开始走gcm推送");
			gcmPushService.sendMsg(receiveMsg, res->{
				if(res.succeeded()){
					logger.info("gcm推送成功");
				}else{
					logger.error("gcm推送失败");
				}
			});
		} else if (PushTypeEnum.XIAOMI.getCode().equals(sendType)) {
			// 只用作对安卓手机进行推送
			logger.info("开始走小米推送");
			xiaomiPushService.sendMsg(receiveMsg, res->{
				if(res.succeeded()){
					logger.info("小米推送成功");
				}else{
					logger.error("小米推送失败");
				}
			});
		} else {
			logger.error("无效推送渠道");
			return;
		}

	}

	private boolean validateRecieveMsg(JsonObject msg) {
		if (msg == null) {
			logger.error("校验未通过，receiveMsg==" + msg);
			return false;
		}
		if (msg.getValue("msgId") == null) {
			logger.error("校验未通过：msgId为空");
			return false;
		}
		if (msg.getValue("customerId") == null) {
			logger.error("校验未通过：customerId为空");
			return false;
		}
		return true;
	}

	/**
	 * 接收消息
	 */
	private void recivedMessage(){
		AmqpBridge bridge = AmqpBridge.create(vertx);
		//读取配置
		Properties prop = PropertiesLoaderUtils.loadProperties( ConnectionConsts.MQ_CONFIG_PATH);
		//bridge.start
		String addr = prop.getProperty(ConnectionConsts.ACTIVEMQ_SERVER_URL);
		int port = Integer.parseInt(prop.getProperty(ConnectionConsts.ACTIVE_SERVER_PORT));
		bridge.start(addr, port, res -> {
			if(!res.succeeded()) {
				System.out.println("Bridge startup failed: " + res.cause());
				return;
			}
			MessageConsumer<JsonObject> consumer = bridge.createConsumer(ConnectionConsts.ACTIVE_QUEUE_TOPIC);
			consumer.handler(vertxMsg -> {
				receiveMsg = vertxMsg.body();
				logger.info("Received a message with body : " + receiveMsg.toString());

				this.consumMsg();
			});
		});
	}


}
