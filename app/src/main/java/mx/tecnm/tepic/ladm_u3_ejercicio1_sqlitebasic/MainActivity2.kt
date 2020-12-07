package mx.tecnm.tepic.ladm_u3_ejercicio1_sqlitebasic

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var baseDatos= BaseDatos(this,"basedatos1", null,1)
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extra=intent.extras
        id=extra?.getString("idactualizar")!!

        TXVActualizar.setText(TXVActualizar.text.toString()+"${id}")
        try {
            var base = baseDatos.readableDatabase
            var respuesta = base.query("persona", arrayOf("nombre","domicilio"),"ID=?", arrayOf(id),null,null,null)

            if(respuesta.moveToFirst()){
                ActualizarNombre.setText(respuesta.getString(0))
                ActualizarDomicilio.setText(respuesta.getString(1))
            }else{
                mensaje("NO se encontro ID")
            }
            base.close()
        }catch (e:SQLiteException){
            mensaje(e.message!!)
        }
        Actualizar.setOnClickListener {
            actualizar(id)
        }
        Regresar.setOnClickListener {
            finish()
        }
    }

    private fun actualizar(id: String) {
        try{
            var trans = baseDatos.writableDatabase
            var valores=ContentValues()
            valores.put("nombre",ActualizarNombre.text.toString())
            valores.put("domicilio",ActualizarDomicilio.text.toString())
            var res=trans.update("persona",valores,"ID=?",arrayOf(id))
            if(res>0){
                mensaje("Se actualizo ID${id}")
                finish()
            }else{
                mensaje("No se pudo actualizar ID")
            }
        }catch (e:SQLiteException){
            mensaje(e.message!!)
        }
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->d.dismiss()}
            .show()
    }
}