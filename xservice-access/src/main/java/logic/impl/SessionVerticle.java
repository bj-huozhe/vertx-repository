package logic.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import logic.SessionService;
import utils.IPUtil;

public class SessionVerticle extends AbstractVerticle implements SessionService {

	private static final Logger logger = LoggerFactory.getLogger(SessionVerticle.class);

	private SharedData sharedData;
	private LocalMap<String, String> sessionMap;// uid -> handlerID
	private LocalMap<String, String> sessionReverse; // handlerID -> uid

	@Override
	public void start() throws Exception {
		sharedData = vertx.sharedData();
		sessionMap = sharedData.getLocalMap("session");
		sessionReverse = sharedData.getLocalMap("sessionReverse");

		String innerIP = IPUtil.getInnerIP();
		EventBus eb = vertx.eventBus();
		eb.<JsonObject>consumer(SessionService.SERVICE_ADDRESS + innerIP, res -> {
			MultiMap headers = res.headers();
			JsonObject body = res.body();
			if (headers != null) {
				String action = headers.get("action");
				String from = body.getString("from");
				String handlerID = body.getString("handlerID");
				logger.info("from={}action={}innerIP={}", from, action, innerIP);
				switch (action) {
				case "setUserSocket":
					res.reply(setUserSocket(from, handlerID));
					break;
				case "delUserSocket":
					res.reply(delUserSocket(from, handlerID));
					break;
				default:
					res.reply(1);// Fail!
					break;
				}
			}
		});

	}

	public int setUserSocket(String uid, String handlerId) {
		this.sessionMap.put(uid, handlerId);
		this.sessionReverse.put(handlerId, uid);

		return 0;
	}

	public int delUserSocket(String uid, String handlerId) {
		this.sessionMap.remove(uid);
		this.sessionReverse.remove(handlerId);

		return 0;
	}

	@Override
	public void getHandlerIDByUid(String uid, Handler<AsyncResult<String>> resultHandler) {
		resultHandler.handle(Future.succeededFuture(sessionMap.get(uid)));
	}

	@Override
	public void getUidByHandlerId(String handlerId, Handler<AsyncResult<String>> resultHandler) {
		resultHandler.handle(Future.succeededFuture(sessionReverse.get(handlerId)));
	}
}
