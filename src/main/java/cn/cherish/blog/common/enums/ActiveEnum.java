package cn.cherish.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否激活
 */
@Getter
@AllArgsConstructor
public enum ActiveEnum {

    ACTIVE(true, "激活/可用"),
    NONE(false, "冻结/不可用"),
    UNKNOWN(false, "未知"),
    ;

    private Boolean active;

    private String desc;

    public static String getDesc(Integer num) {
        ActiveEnum status;
        switch (num) {
            case 1:
                status = ACTIVE;break;
            case 0:
                status = NONE;break;
            default:
                status = UNKNOWN;break;
        }
        return status.getDesc();
    }

}
