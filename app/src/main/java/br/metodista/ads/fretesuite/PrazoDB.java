package br.metodista.ads.fretesuite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import br.metodista.ads.fretesuite.models.MDLCliente;

/**
 * Created by leonardo.lima on 05/05/2017.
 */

public class PrazoDB extends SQLiteOpenHelper {

    public PrazoDB(Context context) {
        super(context, "ClienteDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] arrayServicos = new String[]{"04014", "40045", "40215", "40290", "04510"};
        String[] arrayDescricaoServicos = new String[]{"SEDEX Varejo", "SEDEX a Cobrar Varejo", "SEDEX 10 Varejo",
                "SEDEX Hoje Varejo", "PAC Varejo"};

        db.execSQL(
                "CREATE TABLE Servicos (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",codigo TEXT" +
                        ",descricao TEXT )");


        for (int count = 0; count < arrayServicos.length; count++) {
            db.execSQL("INSERT INTO Servicos (codigo, descricao) VALUES (?, ?)", new Object[]{arrayServicos[count], arrayDescricaoServicos[count]});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Map<String, String> listarServicos() {
        SQLiteDatabase db = getReadableDatabase();
        Map<String, String> mapServicos = new LinkedHashMap<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT descricao, codigo FROM Servicos", null);
            cursor.moveToFirst();

            for (int count = 0; count < cursor.getCount(); count++) {
                mapServicos.put(cursor.getString(0), cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return mapServicos;
    }
}
