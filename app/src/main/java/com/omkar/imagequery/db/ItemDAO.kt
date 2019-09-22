package com.omkar.imagequery.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ItemDAO {

    @Insert
    fun insertAll(items: List<Item>)

    @Query("""SELECT * from items""")
    fun selectAll(): LiveData<List<Item>>

    @Query("SELECT * from items WHERE id = :itemId")
    fun selectById(itemId: Int): LiveData<Item>

    @Query("""DELETE from items where 1""")
    fun deleteAll()

}
