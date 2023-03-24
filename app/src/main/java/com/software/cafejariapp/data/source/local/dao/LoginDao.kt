package com.software.cafejariapp.data.source.local.dao

import androidx.room.*
import com.software.cafejariapp.domain.entity.Login

@Dao
interface LoginDao {

    @Query("SELECT * FROM login")
    suspend fun getLogins(): List<Login>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogin(login: Login)

    @Delete
    suspend fun deleteLogin(login: Login)
}