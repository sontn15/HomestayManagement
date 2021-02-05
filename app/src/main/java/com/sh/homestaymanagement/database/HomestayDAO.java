package com.sh.homestaymanagement.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sh.homestaymanagement.entity.BookingEntity;
import com.sh.homestaymanagement.entity.CustomerEntity;
import com.sh.homestaymanagement.entity.RoomEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface HomestayDAO {

    @Query("SELECT * FROM room")
    List<RoomEntity> findAllRoom();

    @Query("SELECT * from room r where r.id not in " +
            "(select b.room_id from booking b where b.from_date between :fromDate and :toDate) ")
    List<RoomEntity> findAllRoomFree(Long fromDate, Long toDate);

    @Query("SELECT * FROM booking b where b.room_id = :roomId order by b.from_date desc")
    List<BookingEntity> findAllBookingOfRoom(Long roomId);

    @Query("SELECT * FROM booking b where b.room_id = :roomId and b.from_date <= :currentDate and :currentDate <= b.to_date")
    BookingEntity findBookingEntityByDate(Long roomId, Long currentDate);

    @Query("SELECT * FROM CUSTOMER")
    List<CustomerEntity> findAllCustomer();

    @Insert(onConflict = IGNORE)
    void insertRoom(RoomEntity roomEntity);

    @Update(onConflict = REPLACE)
    void updateRoom(RoomEntity roomEntity);

    @Delete
    void deleteRoom(RoomEntity roomEntity);

    @Insert(onConflict = IGNORE)
    long insertCustomer(CustomerEntity customerEntity);

    @Insert(onConflict = IGNORE)
    void insertBooking(BookingEntity bookingEntity);
}