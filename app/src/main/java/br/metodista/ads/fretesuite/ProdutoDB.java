package br.metodista.ads.fretesuite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.metodista.ads.fretesuite.models.MDLProduto;

/**
 * Created by Apartamento on 08/05/2017.
 */

public class ProdutoDB extends SQLiteOpenHelper {
    private static ProdutoDB instance;
    private ProdutoDB(Context context) {
        super(context, "FreteSuite", null, 1);
    }

    public static ProdutoDB getInstance(Context context) {
        if(instance == null) {
            instance = new ProdutoDB(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Produto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descricao VARCHAR(45) NOT NULL," +
                "codigo_rastreio VARCHAR(45) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<MDLProduto> listarProdutos(){
        SQLiteDatabase db = getReadableDatabase();
        List<MDLProduto> produtos = new ArrayList();


        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT id, descricao, codigo_rastreio FROM produto", null);



            while(cursor.moveToNext()){
                MDLProduto produto = new MDLProduto();
                produto.setId(new Long(cursor.getInt(0)));
                produto.setDescricao(cursor.getString(1));
                produto.setCodigoRastreio(cursor.getString(2));

                produtos.add(produto);
            }

            cursor.close();
            return produtos;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return null;
    }

    public MDLProduto salvarProduto(MDLProduto produto) {
        ContentValues values = new ContentValues();

        values.put("descricao",produto.getDescricao());
        values.put("codigo_rastreio", produto.getCodigoRastreio());

        SQLiteDatabase db = getWritableDatabase();

        try {
            if (produto.getId() != null) {
                String[] where = new String[]{String.valueOf(produto.getId())};

                db.update("Produto", values, "id = ?", where);
            } else {
                long id = db.insert("Produto", null, values);
                produto.setId(id);
            }

            return produto;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            db.close();
        }

        return null;
    }

    public boolean apagarProduto(MDLProduto produto) {
        SQLiteDatabase db = getWritableDatabase();

        String[] where = new String[] {String.valueOf(produto.getId())};

        try {
            db.delete("Produto", "id= ?", where);
            return true;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
        finally {
            db.close();
        }
    }
}
