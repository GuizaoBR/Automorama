package com.guizaotech.automorama.database

import androidx.room.*
import com.guizaotech.automorama.modelo.Consumo

@Dao
interface RoomConsumoDao {

    @Query("SELECT c.* FROM Consumo c WHERE idVeiculo = :idVeiculo ORDER BY data ;")
    fun todos(idVeiculo: Long): MutableList<Consumo>

    //@Query("SELECT idConsumo, idVeiculo, consumoTotal, tanqueCompleto FROM Consumo c WHERE tanqueCompleto = 1 AND c.idVeiculo = :idVeiculo GROUP BY idConsumo;")
    @Query("SELECT * FROM Consumo c WHERE tanqueCompleto = 1 AND c.idVeiculo = :idVeiculo GROUP BY idConsumo;")
    fun listaConsumoTanqueCompleto(idVeiculo: Long): MutableList<Consumo>

    @Insert
    fun salva(consumo: Consumo)

    @Delete
    fun remove(consumo: Consumo)

    @Update
    fun altera(consumo: Consumo)

    @Query("SELECT * FROM CONSUMO WHERE idVeiculo = :idVeiculo ORDER BY idVeiculo DESC LIMIT 1  ")
    fun ultimoConsumo(idVeiculo: Long): Consumo
}