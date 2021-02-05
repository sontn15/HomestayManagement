package com.sh.homestaymanagement.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sh.homestaymanagement.utils.DateConverter;

import java.io.Serializable;

@Entity(tableName = "booking")
public class BookingEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "room_id")
    private Long roomId;

    @ColumnInfo(name = "customer_id")
    private Long customerId;

    @ColumnInfo(name = "from_date")
    private Long fromDate;

    @ColumnInfo(name = "to_date")
    @TypeConverters(DateConverter.class)
    private Long toDate;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private Long price;

    public BookingEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
