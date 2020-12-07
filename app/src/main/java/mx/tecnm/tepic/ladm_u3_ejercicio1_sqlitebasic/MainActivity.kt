package mx.tecnm.tepic.ladm_u3_ejercicio1_sqlitebasic

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteQuery
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseDatos= BaseDatos(this,"basedatos1", null,1)
    var listaID=ArrayList<String>()
    var idSeleccionadoEnLista=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            insertar()
        }
        button2.setOnClickListener {
            consultar()
        }
        cargarContactos()

    }

    private fun consultar() {
        try{
            /*
            1.- apertura de base de datos basados en modo LECTURA o ESCRITRA
            2.-Construccion de sentecia SQL
            3.-Ejecucion y mostrado de resultados
            */
            var trans = baseDatos.writableDatabase//permite leer y escribir

            var resp = trans.query("persona", arrayOf("id","nombre","domicilio"),"id=?", arrayOf(idp.text.toString()),null,null,null)


            if(resp.moveToFirst()){
                var cad="ID "+resp.getInt(0)+"\nNombre"+resp.getString(1)+"\nDomicilio"+resp.getString(2)
                
                mensaje(cad)
            }else{
                mensaje("NO se encontraron resultados")
            }

            trans.close()
        }catch (e: SQLiteException){

        }
    }



    private fun insertar() {
        try{
            /*
            1.- apertura de base de datos basados en modo LECTURA o ESCRITRA
            2.-Construccion de sentecia SQL
            3.-Ejecucion y mostrado de resultados
            * */
            var trans = baseDatos.writableDatabase//permite leer y escribir
            var variables = ContentValues()

            variables.put("ID",idp.text.toString().toInt())
            variables.put("nombre",Nombre.text.toString())
            variables.put("domicilio",Domicilio.text.toString())

            var resp = trans.insert("persona",null,variables)
            if(resp==-1L){
                mensaje("Error no se pudo insertar")
            }else{
                mensaje("Se Inserto Con Exito")
                limpiarCampo()
            }
            trans.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!)
        }
        cargarContactos()
    }

    private fun cargarContactos() {
        try{
            var trans = baseDatos.readableDatabase
            var persona=ArrayList<String>()
            //Id:1\n nombre:Sergion Valtierra\ndimicilio

            var respuesta = trans.query("persona", arrayOf("*"),null,null,null,null,null)

            listaID.clear()

            if(respuesta.moveToFirst()){
                do{
                    var conca = "ID: ${respuesta.getInt(0)} \nNombre: ${respuesta.getString(1)}\nDOMICILIO: ${respuesta.getString(2)}"

                    persona.add(conca)
                    listaID.add(respuesta.getInt(0).toString())
                }while(respuesta.moveToNext())

            }else{
                persona.add("NO HAY PERSSONAS INSERTADAS")
            }
            //2 posibles situacione dentro de arraylist
            //  1.-Todas las tuplas resultado
            //  2.-No hoy personas insertadas
            listacontactos.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,persona)

            //ligando el manuppal con lista contactos
            this.registerForContextMenu(listacontactos)

            listacontactos.setOnItemClickListener { parent, view, position, id ->
              idSeleccionadoEnLista = position
                Toast.makeText(this,"Se Selecciono  elemento",Toast.LENGTH_LONG).show()
            }

            trans.close()
        }catch(e:SQLiteException){}
    }

    private fun limpiarCampo() {
        idp.setText("")
        Nombre.setText("")
        Domicilio.setText("")
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->d.dismiss()}
            .show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        var inflaterOb=menuInflater
        //cargar un XML y construir un objeto kotlin a partir de esa carga  = inflate
        inflaterOb.inflate(R.menu.menuppal,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if(idSeleccionadoEnLista==-1){
            mensaje("ERROR! Debes de dar click primero en un itm pra actualizar/borrar")
            return true
        }

        when(item.itemId){
            R.id.itemactulizar->{
                var intent = Intent(this,MainActivity2::class.java)
                intent.putExtra("idactualizar",listaID.get(idSeleccionadoEnLista))
                startActivity(intent)

            }
            R.id.itemaEliminar->{
                var idEliminar = listaID.get(idSeleccionadoEnLista).toInt()
                AlertDialog.Builder(this)
                    .setTitle("ATENCION")
                    .setMessage("Estas seguro que deseas eliminar id : "+idEliminar)
                    .setPositiveButton("Eliminar"){d,i->
                        eliminar(idEliminar)
                    }
                    .setNeutralButton("NO"){d,i->}
                    .show()
            }
            R.id.itemSalir->{

            }
        }
        idSeleccionadoEnLista=-1
        return true
    }

    private fun eliminar(ideliminar: Int) {
        try {
            var trans=baseDatos.writableDatabase
            var res = trans.delete("persona","ID=?", arrayOf(ideliminar.toString()))

            if(res==0){
                mensaje("NO se pudo eliminar")
            }else{
                mensaje("SE PUDO ELIMINAR CON EXITO EL ID: ${ideliminar}")
            }
            trans.close()
        }catch (e:SQLiteException){
            mensaje(e.message!!)
        }
    }
}

