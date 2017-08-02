package logic.impl;

import helper.XProxyHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import logic.C2CService;
import persistence.MongoService;
import protocol.CMDConstants;
import protocol.MessageBuilder;

public class C2CVerticle extends AbstractVerticle implements C2CService {

	private EventBus eb;

	private SharedData sharedData;
	private LocalMap<Long, String> sessionMap;// uid -> handlerID
	private LocalMap<String, Long> sessionReverse; // handlerID -> uid

//	private MongoService mongoService;

	private static final String MONGO_COLLECTION = "message";

	@Override
	public void start() throws Exception {
		sharedData = vertx.sharedData();
		sessionMap = sharedData.getLocalMap("session");
		sessionReverse = sharedData.getLocalMap("sessionReverse");

		XProxyHelper.registerService(C2CService.class, vertx, this, C2CService.SERVICE_ADDRESS);

//		mongoService = MongoService.createProxy(vertx);

		eb = vertx.eventBus();
	}

	public void doWithMsgRequest(JsonObject msg, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject mongoMsg = new JsonObject();
		mongoMsg.put("collection", MONGO_COLLECTION);
		mongoMsg.put("data", msg);

		JsonObject body = msg.getJsonObject("body");
		Long from = body.getLong("from");
		Long to = body.getLong("to");

		if (from != null && to != null) {
			Future<JsonObject> saveF = Future.future();
//			mongoService.saveData(mongoMsg, saveF.completer());

			saveF.setHandler(res -> {
				if (res.succeeded()) {
					String fromHandlerId = sessionMap.get(from);

					// 给FROM发A
					JsonObject aMsgBody = new JsonObject();
					aMsgBody.put("ts", System.currentTimeMillis());
					Buffer aMsg = MessageBuilder.buildMsgHeader(MessageBuilder.HEADER_LENGTH,
							msg.getInteger("clientVersion"), CMDConstants.msg_a, msg.getInteger("seq"),
							aMsgBody.toString().length());
					eb.send(fromHandlerId, aMsg);

					// 给TO发N {ts: 时间戳}
					String toHandlerId = sessionMap.get(to);
					JsonObject nMsgBody = new JsonObject();
					nMsgBody.put("from", from).put("content", body).put("ts", System.currentTimeMillis());
					Buffer nMsg = MessageBuilder.buildMsgHeader(MessageBuilder.HEADER_LENGTH,
							msg.getInteger("clientVersion"), CMDConstants.msg_n, msg.getInteger("seq"),
							nMsgBody.toString().length());

					eb.send(toHandlerId, nMsg);
				} else {

				}
			});
		}
	}

	public void doWithAckRequest(JsonObject msg, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject mongoMsg = new JsonObject();
		mongoMsg.put("collection", MONGO_COLLECTION);
		mongoMsg.put("data", msg);

		JsonObject body = msg.getJsonObject("body");
		Long from = body.getLong("from");
		Long to = body.getLong("to");

		if (from != null && to != null) {
			Future<JsonObject> saveF = Future.future();
//			mongoService.saveData(mongoMsg, saveF.completer());

			saveF.setHandler(res -> {
				if (res.succeeded()) {
					String fromHandlerId = sessionMap.get(from);

					// ack 给FROM发 A
					JsonObject aMsgBody = new JsonObject();
					aMsgBody.put("ts", System.currentTimeMillis());
					Buffer aMsg = MessageBuilder.buildMsgHeader(MessageBuilder.HEADER_LENGTH,
							msg.getInteger("clientVersion"), CMDConstants.ack_a, msg.getInteger("seq"),
							aMsgBody.toString().length());
					eb.send(fromHandlerId, aMsg);

					// ack 给TO发 N {ts: 时间戳}
					String toHandlerId = sessionMap.get(to);
					JsonObject nMsgBody = new JsonObject();
					nMsgBody.put("from", from).put("content", body).put("ts", System.currentTimeMillis());
					Buffer nMsg = MessageBuilder.buildMsgHeader(MessageBuilder.HEADER_LENGTH,
							msg.getInteger("clientVersion"), CMDConstants.ack_n, msg.getInteger("seq"),
							nMsgBody.toString().length());

					eb.send(toHandlerId, nMsg);

					resultHandler.handle(Future.succeededFuture());
				} else {
					resultHandler.handle(Future.failedFuture(res.cause()));
				}
			});
		}
	}
}
