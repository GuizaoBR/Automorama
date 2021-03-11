package com.guizaotech.automorama.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guizaotech.automorama.modelo.Veiculo


@Dao
interface RoomVeiculoDao {
    @Insert
    fun salva(veiculo: Veiculo)

    @Query("SELECT * FROM Veiculo")
    fun todos(): LiveData<List<Veiculo>>

    @Delete
    fun remove(veiculo: Veiculo)

    @Update
    fun altera(veiculo: Veiculo)
}