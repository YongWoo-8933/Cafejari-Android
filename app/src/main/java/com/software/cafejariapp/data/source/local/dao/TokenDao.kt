package com.software.cafejariapp.data.source.local.dao

import androidx.room.*
import com.software.cafejariapp.domain.entity.RefreshToken

@Dao
interface TokenDao {

    @Query("SELECT * FROM refreshToken WHERE id = 0")
    suspend fun getToken(): RefreshToken?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: RefreshToken)

    @Delete
    suspend fun deleteToken(token: RefreshToken)
}
