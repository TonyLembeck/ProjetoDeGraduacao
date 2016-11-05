package br.com.android.sample.view.visualizar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import br.com.android.sample.domain.util.LibraryClass;

/**
 * Created by tony on 04/11/16.
 */
public final class GaleriaFotos {
    private static StorageReference storageRef; //= storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");
    private static DatabaseReference firebaseRef;
    private static ArrayList<Bitmap> imgs;
    private static String idPonto;

    public static ArrayList<Bitmap> getImgs(String novoIdPonto){
        if (imgs == null || (!idPonto.equals(novoIdPonto))) {
            idPonto = novoIdPonto;
            getRefFotos();
        }
        return imgs;
    }

    private static void getRefFotos(){
        imgs = new ArrayList<>();
        firebaseRef = LibraryClass.getFirebase();
        firebaseRef = firebaseRef.child("pontos").child(idPonto).child("fotos");
        //storageRef = LibraryClass.getStorage(idPonto);

        firebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final long ONE_MEGABYTE = 1024 * 1024;

                storageRef.child(dataSnapshot.getKey()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgs.add(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });
    }
}
