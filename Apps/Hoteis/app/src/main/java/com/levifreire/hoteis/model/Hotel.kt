package com.levifreire.hoteis.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.levifreire.hoteis.repository.http.Status
import com.levifreire.hoteis.repository.room.COLUMN_ID
import com.levifreire.hoteis.repository.room.COLUMN_SERVER_ID
import com.levifreire.hoteis.repository.room.TABLE_HOTEL

@Entity(tableName = TABLE_HOTEL, indices = [Index(COLUMN_SERVER_ID, unique = true)])
data class Hotel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0,
    var name: String = "",
    var address: String = "",
    var rating: Float = 0.0F,
    var photoUrl: String = "",
    var serverId: Long? = null,
    var status: Int = Status.INSERT
) {
    override fun toString(): String = name
}