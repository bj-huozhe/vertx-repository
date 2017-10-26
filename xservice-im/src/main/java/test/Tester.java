package test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import module.c2c.C2CVerticle;
import module.persistence.MongoVerticle;
import module.quickphrase.QuickPhraseVerticle;
import server.IMServerVerticle;
import server.RestIMVerticle;

public class Tester {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(IMServerVerticle.class.getName(), readBossOpts().setConfig(config()));
		vertx.deployVerticle(C2CVerticle.class.getName(), readBossOpts().setConfig(config()));

		// vertx.deployVerticle(TCPTest.class.getName());

		/**
		 * Instance should be 1 because of ehcache.
		 */
		// vertx.deployVerticle(SessionVerticle.class.getName());

		/**
		 * Instance should be 1 because of ehcache.
		 */
		// vertx.deployVerticle(SessionVerticle.class.getName(), new
		// DeploymentOptions().setConfig(config()));

		// LoginMsgData t = new LoginMsgData();
		// System.out.println(Json.encodePrettily(t));

		// byte[] s = ByteUtil.unsignedShortToByte2(44);
		// for (byte b : s) {
		// System.out.print(b);
		// }

		vertx.deployVerticle(RestIMVerticle.class.getName(), new DeploymentOptions().setConfig(config()));
		vertx.deployVerticle(QuickPhraseVerticle.class.getName(), new DeploymentOptions().setConfig(config()));

		// vertx.deployVerticle(SensitiveWordsVerticle.class.getName());

		vertx.deployVerticle(MongoVerticle.class.getName(), new DeploymentOptions().setConfig(config()));
	}

	private static JsonObject config() {
		String prop = "{\"mongo\":{\"hosts\":[{\"host\":\"10.20.10.100\",\"port\":27017},{\"host\":\"10.20.10.101\",\"port\":27017},{\"host\":\"10.20.10.102\",\"port\":27017}],\"serverSelectionTimeoutMS\":30000,\"maxPoolSize\":50,\"minPoolSize\":25,\"maxIdleTimeMS\":300000,\"maxLifeTimeMS\":3600000,\"waitQueueMultiple\":10,\"waitQueueTimeoutMS\":10000,\"maintenanceFrequencyMS\":2000,\"maintenanceInitialDelayMS\":500,\"username\":\"im-mc\",\"password\":\"im-mc\",\"authSource\":\"im-mc\",\"db_name\":\"im-mc\",\"connectTimeoutMS\":3000,\"socketTimeoutMS\":3000,\"sendBufferSize\":8192,\"receiveBufferSize\":8192,\"keepAlive\":true,\"heartbeat.socket\":{\"connectTimeoutMS\":3000,\"socketTimeoutMS\":3000,\"sendBufferSize\":8192,\"receiveBufferSize\":8192,\"keepAlive\":true},\"heartbeatFrequencyMS\":1000,\"minHeartbeatFrequencyMS\":500},\"mysql\":{\"mc-im\":{\"host\":\"192.168.0.18\",\"port\":3306,\"maxPoolSize\":100,\"username\":\"sqyc_test\",\"password\":\"sqyc_test@01zhuanche.com\",\"database\":\"mc_im\",\"charset\":\"UTF-8\",\"queryTimeout\":3000}},\"im\":[{\"innerIP\":\"10.10.10.193\",\"node\":\"111.206.162.233:4321\"}],\"file\":{\"upload.file.path.prefix\":\"/u01/projectCAR/xservice/xservice-file/\",\"download.file.server.prefix\":\":9090/mc-file/im/download.json?file=\"},\"service.host\":\"127.0.0.1\"}";

		JsonObject jsonObject = new JsonObject(prop);
		return jsonObject;
	}

	public static DeploymentOptions readBossOpts() {
		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(Runtime.getRuntime().availableProcessors());

		return options;
	}
}
