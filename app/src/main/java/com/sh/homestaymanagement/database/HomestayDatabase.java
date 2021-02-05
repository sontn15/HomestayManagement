package com.sh.homestaymanagement.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sh.homestaymanagement.entity.BookingEntity;
import com.sh.homestaymanagement.entity.CustomerEntity;
import com.sh.homestaymanagement.entity.HomestayEntity;
import com.sh.homestaymanagement.entity.InvoiceEntity;
import com.sh.homestaymanagement.entity.RoomEntity;

@Database(entities = {RoomEntity.class, CustomerEntity.class, BookingEntity.class, InvoiceEntity.class, HomestayEntity.class},
        version = 5)
public abstract class HomestayDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "homestay.db";
    private static HomestayDatabase instance;

    public static synchronized HomestayDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), HomestayDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract HomestayDAO homestayDAO();

}