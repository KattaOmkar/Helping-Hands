package omkar.com.FirestoreHelper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import omkar.com.models.User;

public class FireService {
    public static final String TAG = "FireService.java";
    protected User user = null;
    private FirebaseApp app;
    private FirebaseFirestore firestore;

    public FireService init() {
        this.firestore = FirebaseFirestore.getInstance();
        return this;
    }

    public CollectionReference getUsersRef() {

        return this.firestore.collection("users");
    }

    public DocumentReference getUserDocumentByUserId(String id) {
        Log.d(TAG, "***getUserDocumentByUserId**");
        Log.d(TAG, id);
        return getUsersRef().document(id);
    }

    public void setUserWithData(final User user) {

        Map<String, Object> map = new HashMap<>();
        map.put("UID", user.getUserID());
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("address", user.getAddress());
        map.put("number", user.getNumber());
        Log.d(TAG, user.getUserID());


//        this.firestore.collection("users").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        });

        this.firestore.collection("users").document(map.get("email").toString()).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "******NEW USER successfully written with UID ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    public User populateUserWithRefData(String email) {
        DocumentReference rf = this.getUserDocumentByUserId(email);
        Log.d(TAG, "****populateUserWithRefData*****");
        Log.d(TAG, rf.getPath());
        rf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        user = User.setDataFromDocument(document.getData());
//                        new MainActivity().loadNavHeader(user);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return (user != null) ? user : null;
    }


}
