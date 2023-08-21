package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.BusinessItem;

import java.util.List;

@Dao
public interface BusinessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BusinessItem> businessItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BusinessItem businessItem);

    @Query("SELECT * FROM business")
    LiveData<List<BusinessItem>> getAll();

    @Query("SELECT * FROM business WHERE id = :id")
    LiveData<BusinessItem> getBusinessData(String id);

    @Query("UPDATE business SET isDefault =:isDefault WHERE id =:id")
    void updateBusinessData(boolean isDefault, String id);

    @Query("DELETE FROM business")
    void deleteBusiness();

    @Query("DELETE FROM business WHERE id =:id")
    void deleteById(String id);

    @Query("SELECT COUNT(id) FROM business")
    int getCount();

    @Query("UPDATE business SET name = :name, logo =:logo, address =:address, email =:email, phone =:phone, website =:website WHERE id =:businessId")
    void update(String businessId, String name, String logo, String address, String email, String phone, String website);

    @Query("SELECT * FROM business WHERE isDefault =:isDefault")
    LiveData<BusinessItem> getDefaultBusiness(boolean isDefault);

    @Query("SELECT * FROM business WHERE isDefault =:isDefault")
    BusinessItem getDBBusiness(boolean isDefault);
}
