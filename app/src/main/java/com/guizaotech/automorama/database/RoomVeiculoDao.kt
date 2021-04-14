package com.guizaotech.automorama.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomVeiculoDao {
    @Insert
    fun salva(veiculo: Veiculo)

    @Query("SELECT * FROM Veiculo")
    fun todos(): List<Veiculo>

    @Delete
    fun remove(veiculo: Veiculo)

    @Update
    fun altera(veiculo: Veiculo)

    @Query("SELECT EXISTS (SELECT * FROM Veiculo where placa = :placa and idVeiculo <> :idVeiculo)")
    fun placaExiste(placa: String, idVeiculo: Long): Boolean
}