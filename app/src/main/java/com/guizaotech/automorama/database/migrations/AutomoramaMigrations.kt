package com.guizaotech.automorama.database.migrations

import androidx.room.Update
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.android.synthetic.main.activity_form_consumo.view.*

class AutomoramaMigrations {


    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Veiculo RENAME TO Veiculo_old;")
            database.execSQL("CREATE TABLE Veiculo(idVeiculo INTEGER PRIMARY KEY AUTOINCREMENT, anoFabricacao INTEGER, anoModelo INTEGER, modelo TEXT, apelido TEXT, marca TEXT, placa TEXT, caminhoImagem TEXT);")
            database.execSQL("CREATE UNIQUE INDEX index_Veiculo_placa ON Veiculo (placa);")
            database.execSQL("INSERT INTO Veiculo(anoFabricacao, apelido, anoModelo, modelo, marca, placa, caminhoImagem) SELECT anoFabricacao, apelido, anoModelo, modelo, marca, placa, caminhoImagem FROM Veiculo_old")
            database.execSQL("DROP TABLE Veiculo_old")

        }
    }
    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("ALTER TABLE Consumo RENAME TO Consumo_old;")
            database.execSQL("CREATE TABLE Consumo(idConsumo INTEGER PRIMARY KEY AUTOINCREMENT, kmAtual REAL, combustivel VARCHAR(20), litros REAL,  data TEXT, kmAnterior REAL, consumoTotal REAL, idVeiculo INTEGER, tanqueCompleto INTEGER(1), FOREIGN KEY (idVeiculo) REFERENCES Veiculo(idVeiculo) ON DELETE CASCADE ON UPDATE CASCADE)")
            database.execSQL("INSERT INTO Consumo(idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto) SELECT idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto FROM Consumo_old ")
            database.execSQL("DROP TABLE Consumo_old")

        }
    }
    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Consumo RENAME TO Consumo_old;")
            database.execSQL("CREATE TABLE Consumo(idConsumo INTEGER PRIMARY KEY AUTOINCREMENT, kmAtual INTEGER, combustivel VARCHAR(20), litros REAL,  data TEXT, kmAnterior INTEGER, consumoTotal REAL, idVeiculo INTEGER, tanqueCompleto INTEGER(1), FOREIGN KEY (idVeiculo) REFERENCES Veiculo(idVeiculo) ON DELETE CASCADE ON UPDATE CASCADE)")
            database.execSQL("INSERT INTO Consumo(idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto) SELECT idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto FROM Consumo_old ")
            database.execSQL("DROP TABLE Consumo_old")

        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE Manutencao(idManutencao INTEGER PRIMARY KEY AUTOINCREMENT, valor DOUBLE, descricao VARCHAR(500),  data TEXT,  idVeiculo INTEGER, FOREIGN KEY (idVeiculo) REFERENCES Veiculo(idVeiculo) ON DELETE CASCADE ON UPDATE CASCADE)")
            database.execSQL("ALTER TABLE Consumo ADD COLUMN  valor REAL not null default 0.0")
            database.execSQL("update Consumo set kmAnterior = 0 where kmAnterior is null" )
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Manutencao ADD COLUMN titulo TEXT not null default '' ")
        }
    }
    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Manutencao ADD COLUMN feito INTEGER(1) not null default 0 ")
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE Consumo RENAME TO Consumo_old;")
//            database.execSQL("CREATE TABLE Consumo(idConsumo INTEGER PRIMARY KEY AUTOINCREMENT, kmAtual INTEGER, combustivel TEXT, litros REAL,  data TEXT, kmAnterior INTEGER, consumoTotal REAL, idVeiculo INTEGER, tanqueCompleto INTEGER(1), valor REAL not null default 0.0, FOREIGN KEY (idVeiculo) REFERENCES Veiculo(idVeiculo) ON DELETE CASCADE ON UPDATE CASCADE)")
//            database.execSQL("INSERT INTO Consumo(idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto) SELECT idConsumo, kmAtual, combustivel, litros,  data, kmAnterior, consumoTotal, idVeiculo, tanqueCompleto FROM Consumo_old ")
//            database.execSQL("DROP TABLE Consumo_old")
        }
    }

}