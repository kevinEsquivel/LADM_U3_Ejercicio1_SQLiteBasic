package mx.tecnm.tepic.ladm_u3_ejercicio1_sqlitebasic

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    //context: El ACTIVITY que llamara la conexion
    //name: El nombre dek archivo de la base de datos
    //factory: Objeto cursor para navegar entre tplas resultado ACTUALMENTE OBSOLETO
    //Version:La version de la BD Id,NOMbre,Dom=  1version
    ///                 mas adelanta agregar EDAD=2Version
    override fun onCreate(db: SQLiteDatabase) {
        // AMBOS METODOS SIRVEN PARA CONSTRUIR LA ESTRUCTURA
        db.execSQL("Create table persona(ID INTEGER not null primary key,nombre varchar(200),domicilio varchar(200))")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Tambien sirve para construir estructura de DB pero contcretamente actualizaciones
        //UPTADE= actualizacion menor =Camcion en datos almacenados
        //UPGRADE= Actualizacion mayor= Cambois en estrutura de tablas
    }
}