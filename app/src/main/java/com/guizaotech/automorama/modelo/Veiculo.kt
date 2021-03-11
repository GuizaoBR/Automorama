package com.guizaotech.automorama.modelo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index(value = ["placa"], unique = true)])
class Veiculo: Serializable{

    @PrimaryKey(autoGenerate = true)
    var idVeiculo : Long = 0
    var modelo : String = ""
    var anoFabricacao : Int = 0
    var marca : String = ""
    var anoModelo : Int = 0
    var placa : String = ""
    var apelido : String = ""
    var caminhoImagem: String = ""



    override fun toString(): String {
        var veiculo = ""
        if (apelido == ""){
            veiculo = "$marca $modelo $anoFabricacao/$anoModelo-$placa"
        }else{
            veiculo = apelido
        }
        return veiculo
    }


}