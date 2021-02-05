package com.sh.homestaymanagement.domain;

import com.sh.homestaymanagement.entity.RoomEntity;

import java.io.Serializable;
import java.util.Date;

public class RoomDomain implements Serializable {

    private RoomEntity roomEntity;

    private Long fromDate;

    private Long toDate;

    private Long day;

    private Long price;


    public RoomDomain() {
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
