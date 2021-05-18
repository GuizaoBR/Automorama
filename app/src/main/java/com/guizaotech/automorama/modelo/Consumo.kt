package com.guizaotech.automorama.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.guizaotech.automorama.database.DateConverter
import java.io.Serializable
import java.time.LocalDate
import java.util.*

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
    @TypeConverters(DateConverter::class)
    var data : Date = Calendar.getInstance().time
    var consumoTotal : Double = 0.0
    var tanqueCompleto : Boolean = false
    var idVeiculo: Long = 0
    var valor: Double = 0.0






}
