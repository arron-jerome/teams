package com.disney.teams.model.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "ResultVO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultVO implements Serializable {

    private static final long serialVersionUID = -6105070616503910307L;

    private static final int DEFAULT_MAP_SIZE = 1;

    public static final String EMPTY = "";

    private static final String OK_CODE = BaseStatusCode.SUCCESS_CODE;

    private static final String SERVER_ERROR_CODE = BaseStatusCode.SERVER_ERROR_CODE;


    /**
     * exception-code
     */
    String code;
    /**
     * prompt message
     */
    String msg = EMPTY;
    /**
     * trace id for trace linking
     */
    String traceId;
    /**
     * return data
     */
    Object data;

    public ResultVO() {
        super();
    }

    public ResultVO(boolean ok) {
        this();
        this.code = ok ? OK_CODE : SERVER_ERROR_CODE;
    }

    public ResultVO(boolean ok, String msg) {
        this(ok);
        this.msg = msg;
    }

    public ResultVO(boolean ok, String msg, Object data) {
        this(ok, msg);
        this.data = data;
    }

    public ResultVO(String code) {
        this();
        this.code = code;
    }

    public ResultVO(String code, String msg) {
        this(code);
        this.msg = msg;
    }

    public ResultVO(String code, String msg, Object data) {
        this(code, msg);
        this.data = data;
    }

    public ResultVO(BaseStatusCode code) {
        this();
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

    public ResultVO(BaseStatusCode code, Object data) {
        this(code);
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = (msg == null ? EMPTY : msg);
    }

    public String getMsg() {
        return msg;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T dataValue(String key) {
        if (data == null) {
            return null;
        }
        return (T) ((Map<String, ?>) data).get(key);
    }

    public static ResultVO resultVO(boolean ok) {
        return new ResultVO(ok);
    }

    public static ResultVO resultVO(boolean ok, String msg) {
        return new ResultVO(ok, msg);
    }

    public static ResultVO resultVO(boolean ok, String msg, Object data) {
        return new ResultVO(ok, msg, data);
    }

    public static ResultVO resultVO(BaseStatusCode code) {
        return new ResultVO(code);
    }

    public static ResultVO resultVO(BaseStatusCode code, Object data) {
        return new ResultVO(code, data);
    }

    public static ResultVO resultVO(String code, String msg) {
        return new ResultVO(code, msg);
    }

    public static ResultVO resultVO(String code, String msg, Object data) {
        return new ResultVO(code, msg, data);
    }

    public static ResultVO ok() {
        return resultVO(true);
    }

    public static ResultVO ok(Object data) {
        return resultVO(BaseStatusCode.SUCCESS).data(data);
    }

    public static ResultVO failed() {
        return resultVO(false);
    }

    public static ResultVO failed(String msg) {
        return resultVO(false, msg);
    }

    public static ResultVO failed(String code, String msg) {
        return resultVO(code, msg);
    }

    public static ResultVO failed(String code, String msg, Object data) {
        return resultVO(code, msg).data(data);
    }

    public static ResultVO failed(BaseStatusCode code) {
        return resultVO(code);
    }

    public static ResultVO failed(BaseStatusCode code, Object data) {
        return resultVO(code).data(data);
    }

    public static boolean isOk(ResultVO resultVO) {
        return null != resultVO && OK_CODE.equals(resultVO.getCode());
    }

    public static boolean isOk(String code) {
        return OK_CODE.equals(code);
    }

    public static boolean isOk(BaseStatusCode code) {
        return code != null && OK_CODE.equals(code.getCode());
    }

    public static boolean isNotOk(ResultVO resultVO) {
        return !isOk(resultVO);
    }

    public static boolean isNotOk(String code) {
        return !isOk(code);
    }

    public static boolean isNotOk(BaseStatusCode code) {
        return !isOk(code);
    }

    public static boolean isAllOk(Collection<ResultVO> resultVOS) {
        if (resultVOS == null || resultVOS.isEmpty()) {
            return true;
        }
        for (ResultVO resultVO : resultVOS) {
            if (isNotOk(resultVO)) {
                return false;
            }
        }
        return true;
    }

    public ResultVO code(String code) {
        this.code = code;
        return this;
    }

    public ResultVO code(BaseStatusCode code) {
        this.code = code.getCode();
        return this;
    }

    public ResultVO resultVO(String msg) {
        setMsg(msg);
        return this;
    }

    public ResultVO data(Object data) {
        this.data = data;
        return this;
    }

    public <T> T data() {
        return (T) data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Msg [code=").append(code)
                .append(", msg=").append(msg)
                .append(", data=").append(data)
                .append("]");
        return sb.toString();
    }

    public String toJsonStringWithoutData() {
        String format = "{\"code\":\"%s\",\"msg\":\"%s\", \"traceId\":%s}";
        return String.format(format, code, msg == null ? "" : msg, traceId == null ? null : "\"" + traceId + "\"");
    }

    public static ResultVO parseFromJsonStringWithoutData(String json) {
        if (json == null) {
            return null;
        }
        String code = null;
        int codeIndex = json.indexOf("code");
        if (codeIndex >= 0) {
            codeIndex += 7;
            int quoteIndex = json.indexOf('"', codeIndex);
            if (quoteIndex >= 0) {
                code = json.substring(codeIndex, quoteIndex);
            }
        }

        String traceId = null;
        int traceIdIndex = json.indexOf("traceId");
        if (traceIdIndex >= 0) {
            traceIdIndex += 6;
            int quoteIndex = json.indexOf('"', traceIdIndex);
            if (quoteIndex >= 0) {
                traceId = json.substring(traceIdIndex, quoteIndex);
            }
        }

        String msg = "";
        int msgIndex = json.indexOf("msg");
        if (msgIndex >= 0) {
            msgIndex += 6;
            int quioteIndex = msgIndex;
            while ((quioteIndex = json.indexOf('"', quioteIndex)) >= 0) {
                if (json.charAt(quioteIndex - 1) != '\\') {
                    msg = json.substring(msgIndex, quioteIndex);
                    break;
                }
            }
        }
        ResultVO resultVO = resultVO(code, msg);
        resultVO.setTraceId(traceId);
        return resultVO;
    }
}
