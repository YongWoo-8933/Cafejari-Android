package com.software.cafejariapp.data.source.local.dao

import androidx.room.*
import com.software.cafejariapp.domain.entity.DisableDate

@Dao
interface DisabledDateDao {

    @Query("SELECT * FROM DisableDate")
    suspend fun getDisabledDates(): List<DisableDate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDisabledDate(disabledDate: DisableDate)

}