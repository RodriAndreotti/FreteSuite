package br.metodista.ads.fretesuite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.metodista.ads.fretesuite.models.MDLCliente;

/**
 * Created by leonardo.lima on 05/05/2017.
 */

public class ClienteDB extends SQLiteOpenHelper {

    public ClienteDB(Context context) {
        super(context, "ClienteDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE Clientes (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",nome TEXT" +
                        ",cep TEXT" +
                        ",endereco TEXT" +
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public MDLCliente salvarCliente(MDLCliente mdlCliente) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        long id = 0;

        try {
            values.put("Cep", mdlCliente.getCep());
            values.put("Endereco", mdlCliente.getEndereco());
            values.put("Nome", mdlCliente.getNome());

            //Verifica se é uma inclusão ou alteração
            if (mdlCliente.get_id() == null) {
                //Inclusão

                id = db.insert("Clientes", null, values);
                mdlCliente.set_id(id);
            } else {
                //Alteração

                String[] where = new String[]{String.valueOf(mdlCliente.get_id())};

                db.update("Clientes", values, "_id = ?", where);
            }
        } finally {
            db.close();
        }
        return mdlCliente;
    }

    public void removerCliente(Long idCliente) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String[] where = new String[]{String.valueOf(idCliente)};
            db.delete("Clientes", "_id = ?", where);
        } finally {
            db.close();
        }
    }

    public List<MDLCliente> listarClientes() {
        SQLiteDatabase db = getReadableDatabase();
        List<MDLCliente> lista = new ArrayList();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT _id, nome, cep, endereco FROM Clientes", null);

            cursor.moveToFirst();

            for (int count = 0; count < cursor.getCount(); count++) {
                lista.add(new MDLCliente(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return lista;
    }
}
