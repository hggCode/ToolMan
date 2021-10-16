package work.wlwl.toolman.service.base.exception;


import lombok.Data;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;

@Data
public class GlobalException  extends RuntimeException{

    //状态码
    private Integer code;

    /**
     *接受状态码和消息
     * @param message
     * @param code
     */
    public GlobalException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型
     *
     * @param resultCodeEnum
     */
    public GlobalException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
