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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omkar.com.models.LoanGroup;
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

    String res = "Fail";

    public CollectionReference getLoansCollection() {
        return this.firestore.collection("loanGroups");
    }

    public String createNewLoan(LoanGroup loanGroup) {

        if (loanGroup == null) {
            return "null loangroup";
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("borrowerID", loanGroup.getBorrowerId());
        map.put("borrowerName", loanGroup.getBorrowerName());
        map.put("reason", loanGroup.getReason());
        map.put("phone", loanGroup.getPhone());
        map.put("borrowerAddress", loanGroup.getBorrowerAddress());
        map.put("amount", loanGroup.getAmount());
        map.put("amountBorrowed", new Float(0));
        map.put("date", loanGroup.getDate());
        map.put("interest", loanGroup.getInterest());
        map.put("tenureDays", loanGroup.getTenureDays());
        Log.d(TAG, "createNewLoan: ***********************");
        Log.d(TAG, map.toString());
        this.getLoansCollection().document(map.get("borrowerID").toString() + "__" + map.get("date").toString())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        res = "SUCCESS";
                    }
                });

        return res;
    }

    public List<LoanGroup> getAllLoans() {
        final List<LoanGroup> loanGroups = new ArrayList<>();
        this.getLoansCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> ds = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot data : ds) {
                        Map<String, Object> jsonD = data.getData();
                        Log.d(TAG, jsonD.toString());
                        final LoanGroup holder = LoanGroup.makeFromMap(jsonD);
                        loanGroups.add(holder);
                        Log.d(TAG, "onSuccess: loanGroups count " + loanGroups.size());
                    }
                }
            }
        });


        return loanGroups;
    }

}
