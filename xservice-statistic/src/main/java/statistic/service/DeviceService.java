package statistic.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.serviceproxy.ProxyHelper;
import statistic.service.dto.DeviceDto;
import utils.BaseResponse;
import utils.IPUtil;

/**
 * Created by lufei
 * Date : 2017/7/25 13:32
 * Description :
 */
@ProxyGen
@VertxGen
public interface DeviceService {
    public static final String SERVICE_NAME = "http.userDevice.eb.service";

    public static final String SERVICE_ADDRESS = "http-userDevice-eb-service";

    public static final String LOCAL_SERVICE_NAME = "local.http.userDevice.eb.service";

    static DeviceService createProxy(Vertx vertx) {
        return ProxyHelper.createProxy(DeviceService.class, vertx, DeviceService.SERVICE_ADDRESS);
    }

    static DeviceService createLocalProxy(Vertx vertx) {
        return ProxyHelper.createProxy(DeviceService.class, vertx, getLocalAddress(),
                new DeliveryOptions().setSendTimeout(3000));
    }

    static String getLocalAddress() {
        return new StringBuffer().append(IPUtil.getInnerIP()).append("-").append(SERVICE_ADDRESS).toString();
    }


    /**
     * @param userDeviceDto
     * @param result
     */
    void reportUserDevice(DeviceDto userDeviceDto, Handler<AsyncResult<BaseResponse>> result);

}
