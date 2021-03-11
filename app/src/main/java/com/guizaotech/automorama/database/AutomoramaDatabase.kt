package com.guizaotech.automorama.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.guizaotech.automorama.database.migrations.AutomoramaMigrations
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Veiculo


@Database(entities = [Veiculo::class, Consumo::class, Manutencao::class], version = 9)
abstract class AutomoramaDatabase: RoomDatabase() {

    abstract fun getRoomVeiculoDAO(): RoomVeiculoDao

    abstract fun getRoomConsumoDAO(): RoomConsumoDao

    abstract fun getRoomManutencaoDAO(): RoomManutencaoDao

    //Funções Estáticas
    companion object {
        fun getInstance(context: Context): AutomoramaDatabase {
            val migrations = AutomoramaMigrations()
            return Room
                .databaseBuilder(context, AutomoramaDatabase::class.java, "Automorama")
                .addMigrations(migrations.MIGRATION_3_4,
                    migrations.MIGRATION_4_5,
                    migrations.MIGRATION_5_6,
                    migrations.MIGRATION_6_7,
                    migrations.MIGRATION_7_8,
                    migrations.MIGRATION_8_9)
                .build()
        }
    }



}