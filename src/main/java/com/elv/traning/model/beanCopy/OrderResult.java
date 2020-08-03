package com.elv.traning.model.beanCopy;

import java.io.Serializable;
import java.util.List;

import com.elv.core.annotation.desensitization.Blur;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class OrderResult implements Serializable {

    private static final long serialVersionUID = 2323027276494000422L;

    private String channelOrderNo;
    private long channelPrice;
    private String orderNo;

    @Blur(fromIdx = 1, ratio = 1.0D, mask = "#")
    private String contacter;

    @Blur(fromIdx = 6, setpSize = 3, mask = "#")
    private String mobile;

    @Blur(fromIdx = 1, setpSize = 3)
    private long invoicePrice;
    private List<OrderRoomResult> orderRoomEntities;

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public long getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(long channelPrice) {
        this.channelPrice = channelPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getContacter() {
        return contacter;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(long invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public List<OrderRoomResult> getOrderRoomEntities() {
        return orderRoomEntities;
    }

    public void setOrderRoomEntities(List<OrderRoomResult> orderRoomEntities) {
        this.orderRoomEntities = orderRoomEntities;
    }
}
