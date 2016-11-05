package br.com.android.sample.domain.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public final class LibraryClass {
    public static String PREF = "br.com.android.sample.domain.util.LibratyClass.PREF";
    private static DatabaseReference firebase;
    private static StorageReference storageRef;
    private static String idPonto;

    private LibraryClass(){}

    public static DatabaseReference getFirebase(){
        if( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return( firebase );
    }
    public static StorageReference getStorage(String novoIdPonto){

        if( storageRef == null || (!novoIdPonto.equals(idPonto))){
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com").child("fotos/" + novoIdPonto);
            idPonto = novoIdPonto;
        }
        return( storageRef );
    }
    public static StorageReference getStorage(){

        if( storageRef == null ){
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");
        }
        return( storageRef );
    }

    static public void saveSP(Context context, String key, String value ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    static public String getSP(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return( token );
    }
}