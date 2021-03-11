package com.guizaotech.automorama.database

import androidx.room.*
import com.guizaotech.automorama.modelo.Manutencao

@Dao
interface RoomManutencaoDao {

    @Query("SELECT m.* FROM Manutencao m WHERE idVeiculo = :idVeiculo GROUP BY idVeiculo ORDER BY data;")
    fun todos(idVeiculo: Long): MutableList<Manutencao>

    @Insert
    fun salva(manutencao: Manutencao)

    @Delete
    fun remove(manutencao: Manutencao)

    @Update
    fun altera(manutencao: Manutencao)
}