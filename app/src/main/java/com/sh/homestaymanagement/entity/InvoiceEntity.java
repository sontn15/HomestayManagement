package com.sh.homestaymanagement.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sh.homestaymanagement.utils.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "invoice")
public class InvoiceEntity  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "booking_id")
    private Long bookingId;

    @ColumnInfo(name = "created_date")
    @TypeConverters(DateConverter.class)
    private Date createdDate;

    @ColumnInfo(name = "total_price")
    private Long totalPrice;

    @ColumnInfo(name = "status")
    private Boolean status;

    public InvoiceEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
