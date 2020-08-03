package com.elv.traning.model.beanCopy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class OrderEntity extends ChannelOrderEntity {

    private String orderNo;
    private String contacter;
    private String mobile;
    private int status;
    private Long price;
    private long invoicePrice;
    private List<OrderRoomEntity> orderRoomEntities;
    private List<String> remarks;
    private Set<String> notes;
    private List<Map<String, String>> mapList;
    private Map<String, OrderRoomEntity> orderRoomMap;
    private Map<String, Integer> logMap;
    private Date date;
    private OrderRoomEntity orderRoomEntity;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public long getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(long invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public List<OrderRoomEntity> getOrderRoomEntities() {
        return orderRoomEntities;
    }

    public void setOrderRoomEntities(List<OrderRoomEntity> orderRoomEntities) {
        this.orderRoomEntities = orderRoomEntities;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public List<Map<String, String>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, String>> mapList) {
        this.mapList = mapList;
    }

    public Map<String, OrderRoomEntity> getOrderRoomMap() {
        return orderRoomMap;
    }

    public void setOrderRoomMap(Map<String, OrderRoomEntity> orderRoomMap) {
        this.orderRoomMap = orderRoomMap;
    }

    public Map<String, Integer> getLogMap() {
        return logMap;
    }

    public void setLogMap(Map<String, Integer> logMap) {
        this.logMap = logMap;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<String> getNotes() {
        return notes;
    }

    public void setNotes(Set<String> notes) {
        this.notes = notes;
    }

    public OrderRoomEntity getOrderRoomEntity() {
        return orderRoomEntity;
    }

    public void setOrderRoomEntity(OrderRoomEntity orderRoomEntity) {
        this.orderRoomEntity = orderRoomEntity;
    }

}
