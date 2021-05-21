package com.guizaotech.automorama.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.guizaotech.automorama.database.DateConverter
import java.io.Serializable
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = Veiculo::class, parentColumns = ["idVeiculo"],
    childColumns = ["idVeiculo"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE )])
class Manutencao : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idManutencao : Long? = null
    var valor : Float = 0F
    var descricao : String = ""
    var titulo : String = ""
    @TypeConverters(DateConverter::class)
    var data : Date = Calendar.getInstance().time
    var idVeiculo: Long = 0
    var feito: Boolean = false

}
