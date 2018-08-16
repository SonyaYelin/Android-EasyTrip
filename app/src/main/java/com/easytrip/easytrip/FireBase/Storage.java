package com.easytrip.easytrip.FireBase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.easytrip.easytrip.FireBase.IFireBaseConstants.JPG;

public class Storage {

    private static Storage      storage;
    private StorageReference    storageRef;

    private Storage(){
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public static Storage getInstance(){
        if (storage == null)
            storage = new Storage();
        return storage;
    }

    public void uploadImage(String userId, ImageView imageView) {
        // Create a reference to "userId.jpg"
        StorageReference mountainsRef = storageRef.child(userId + JPG);

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        BitmapDrawable bitmapDrawable= (BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap = (bitmapDrawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.getMessage();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    public void getProfileImage(String userId, final ImageView imageView){
        StorageReference islandRef = storageRef.child(userId + JPG);

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.getMessage();
            }
        });
    }
}

