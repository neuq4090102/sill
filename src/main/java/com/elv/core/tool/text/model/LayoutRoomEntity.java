package com.elv.core.tool.text.model;

import java.util.Date;

/**
 * @author lxh
 * @since 2020-08-27
 */
public class LayoutRoomEntity {

    private long id;
    private long hotelId;
    private long layoutId;
    private long roomId;
    private Date createTime;
    private Date updateTime;

    public LayoutRoomEntity() {
    }

    public LayoutRoomEntity(long hotelId, long layoutId, long roomId) {
        this.hotelId = hotelId;
        this.layoutId = layoutId;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(long layoutId) {
        this.layoutId = layoutId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
