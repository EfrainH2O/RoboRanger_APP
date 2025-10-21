package com.example.roboranger.data.local.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IFormsDao {

    @Query("Select * from Forms_1 where id = :id")
    suspend fun getForms1_AllInfo(id: Int): Forms_1?

    @Query("Select * from Forms_2 where id = :id")
    suspend fun getForms2_AllInfo(id: Int): Forms_2?

    @Query("UPDATE Forms_1 SET enviado = 1 WHERE id = :formId")
    suspend fun markForm1AsSent(formId: Int)

    @Query("UPDATE Forms_2 SET enviado = 1 WHERE id = :formId")
    suspend fun markForm2AsSent(formId: Int)

    @Query("""
        SELECT 
            (SELECT COUNT(id) FROM Forms_1 WHERE enviado = 1) 
            + 
            (SELECT COUNT(id) FROM Forms_2 WHERE enviado = 1)
    """)
    suspend fun getFormsCount(): Int

    @Query("""
        SELECT id, clima, temporada, latitude, longitude, fecha, enviado, 1 as formType FROM Forms_1
        UNION ALL
        SELECT id, clima, temporada, latitude, longitude, fecha, enviado, 2 as formType FROM Forms_2
    """)
    suspend fun getAllCommonForms(): List<CommonFormData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForm1(form: Forms_1)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForm2(form: Forms_2)
}

data class CommonFormData(
    val id: Int,
    val clima: ClimaType,
    val temporada: EpocaType,
    val latitude: Double,
    val longitude: Double,
    val fecha: String,
    val enviado: Boolean,
    val formType: Int // 1 para Forms_1, 2 para Forms_2
)