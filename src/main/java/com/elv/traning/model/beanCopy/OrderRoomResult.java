package com.elv.traning.model.beanCopy;

import java.io.Serializable;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class OrderRoomResult implements Serializable {

    private static final long serialVersionUID = 6489994932604273643L;

    private String orderRoomNo;
    private boolean halfDay;

    public String getOrderRoomNo() {
        return orderRoomNo;
    }

    public void setOrderRoomNo(String orderRoomNo) {
        this.orderRoomNo = orderRoomNo;
    }

    public boolean isHalfDay() {
        return halfDay;
    }

    public void setHalfDay(boolean halfDay) {
        this.halfDay = halfDay;
    }
}
