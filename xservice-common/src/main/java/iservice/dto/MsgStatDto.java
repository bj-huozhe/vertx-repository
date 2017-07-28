package iservice.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Created by lufei
 * Date : 2017/7/26 15:56
 * Description :
 */
@DataObject(generateConverter = true)
public class MsgStatDto {
    //应用code
    private Integer appCode;
    //消息id
    private String msgId;
    //消息发送动作,参照：PushActionEnum
    private Integer action;
    //渠道，参照：ChannelEnum
    private Integer channel;
    //消息发送时间
    private String sendTime;
    //消息到达时间
    private String arriveTime;
    //设备类型,参照：OsTypeEnum
    private Integer osType;
    //imei
    private String imei;
    //蚂蚁金服指纹
    private String antFingerprint;
    //是否接收推送消息 1：是 0 否
    private Integer isAcceptPush;


    public MsgStatDto() {
        super();
    }

    public MsgStatDto(JsonObject json) {
        // A converter is generated to easy the conversion from and to JSON.
        MsgStatDtoConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        MsgStatDtoConverter.toJson(this, json);
        return json;
    }

    public Integer getAppCode() {
        return appCode;
    }

    public void setAppCode(Integer appCode) {
        this.appCode = appCode;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getOsType() {
        return osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public Integer getIsAcceptPush() {
        return isAcceptPush;
    }

    public void setIsAcceptPush(Integer isAcceptPush) {
        this.isAcceptPush = isAcceptPush;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAntFingerprint() {
        return antFingerprint;
    }

    public void setAntFingerprint(String antFingerprint) {
        this.antFingerprint = antFingerprint;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MsgStatDto{");
        sb.append("appCode=").append(appCode);
        sb.append(", msgId='").append(msgId).append('\'');
        sb.append(", action=").append(action);
        sb.append(", channel=").append(channel);
        sb.append(", sendTime='").append(sendTime).append('\'');
        sb.append(", arriveTime='").append(arriveTime).append('\'');
        sb.append(", osType=").append(osType);
        sb.append(", imei='").append(imei).append('\'');
        sb.append(", antFingerprint='").append(antFingerprint).append('\'');
        sb.append(", isAcceptPush=").append(isAcceptPush);
        sb.append('}');
        return sb.toString();
    }
}
