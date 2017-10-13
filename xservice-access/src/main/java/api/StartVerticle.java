package api;

import cluster.impl.ConsistentHashingVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import logic.impl.C2CVerticle;
import logic.impl.IMSessionVerticle;
import persistence.impl.MongoVerticle;
import server.RestIMVerticle;
import server.IMServerVerticle;

public class StartVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(StartVerticle.class);

	public void start() throws Exception {
		vertx.deployVerticle(IMServerVerticle.class.getName(), readBossOpts().setConfig(config()));
		vertx.deployVerticle(ConsistentHashingVerticle.class.getName(), readBossOpts().setConfig(config()));
		vertx.deployVerticle(RestIMVerticle.class.getName(), readBossOpts().setConfig(config()));
		vertx.deployVerticle(C2CVerticle.class.getName(), readBossOpts().setConfig(config()));

		vertx.deployVerticle(MongoVerticle.class.getName(), readBossOpts().setConfig(config()));

		/**
		 * Instance should be 1 because of ehcache.
		 */
		vertx.deployVerticle(IMSessionVerticle.class.getName());
	};

	public static DeploymentOptions readBossOpts() {
		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(Runtime.getRuntime().availableProcessors());

		logger.info("Runtime.getRuntime().availableProcessors()={}", Runtime.getRuntime().availableProcessors());

		return options;
	}
}
