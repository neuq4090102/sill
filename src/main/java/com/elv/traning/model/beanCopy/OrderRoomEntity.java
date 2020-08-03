package com.elv.traning.model.beanCopy;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class OrderRoomEntity extends BasicEntity {

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
