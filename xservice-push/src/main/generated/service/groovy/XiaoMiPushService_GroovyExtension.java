package service.groovy;
public class XiaoMiPushService_GroovyExtension {
  public static void sendMsg(service.XiaoMiPushService j_receiver, java.util.Map<String, Object> recieveMsg, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.sendMsg(recieveMsg != null ? io.vertx.core.impl.ConversionHelper.toJsonObject(recieveMsg) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<utils.BaseResponse>>() {
      public void handle(io.vertx.core.AsyncResult<utils.BaseResponse> ar) {
        resultHandler.handle(ar.map(event -> event != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(event.toJson()) : null));
      }
    } : null);
  }
}
