package com.guizaotech.automorama.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

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
    var data : String = ""
    var idVeiculo: Long = 0
    var feito: Boolean = false

}
