package com.guizaotech.automorama.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(foreignKeys = [ForeignKey(entity = Veiculo::class, parentColumns = ["idVeiculo"],
    childColumns = ["idVeiculo"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE )])
class Consumo : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idConsumo : Long = 0
    var kmAtual : Int = 0
    var kmAnterior : Int = 0
    var litros : Double = 0.0
    var combustivel : String = ""
    var data : String = ""
    var consumoTotal : Double = 0.0
    var tanqueCompleto : Int = 0
    var idVeiculo: Long = 0
    var valor: Double = 0.0






}
