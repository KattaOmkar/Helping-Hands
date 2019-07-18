package omkar.com.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class LoanGroup {
    private ArrayList<Lender> lenders = new ArrayList<Lender>();
    private String borrowerAddress;
    private String reason;
    private float amount;
    private float amountBorrowed;
    private String borrowerId;
    private String borrowerName;
    private long date;
    private float interest;
    private String phone;
    private int tenureDays;

    public LoanGroup() {
    }

    public static LoanGroup makeFromMap(final Map<String, Object> map) {
        Gson gson = new Gson();
        map.put("date", System.currentTimeMillis());
        LoanGroup g = gson.fromJson(map.toString(), LoanGroup.class);
//        LoanGroup g = new LoanGroup();
//        Log.d("LOAN GRP", map.toString());
//        Log.d("LOAN GRP", g.borrowerId);
//        Log.d("LOAN GRP", g.borrowerName);
//        Log.d("LOAN GRP", g.borrowerAddress);

//        if ((map != null) && (!map.isEmpty())) {
//            if (! map.get("borrowerID").equals("Borrowerid")) {
//                g.setBorrowerId(map.get("borrowerID").toString());
//                g.setDate(Long.parseLong(map.get("date").toString()));
//                g.setBorrowerName(map.get("borrowerName").toString());
//                g.setBorrowerAddress(map.get("borrowerAddress").toString());
//                g.setReason(map.get("reason").toString());
//                g.setPhone(map.get("phone").toString());
//                g.setAmount(Float.parseFloat(map.get("amount").toString()));
//                g.setAmountBorrowed(Float.parseFloat(map.get("amountBorrowed").toString()));
//                g.setInterest(Float.parseFloat(map.get("interest").toString()));
//                g.setTenureDays(Integer.parseInt(map.get("tenureDays").toString()));


//            }
//        }
        return g;
    }

    public ArrayList<Lender> getLenders() {
        return lenders;
    }

    public void setLenders(ArrayList<Lender> lenders) {
        this.lenders = lenders;
    }

    public String getBorrowerAddress() {
        return borrowerAddress;
    }

    public void setBorrowerAddress(String borrowerAddress) {
        this.borrowerAddress = borrowerAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmountBorrowed() {
        return amountBorrowed;
    }

    public void setAmountBorrowed(float amountBorrowed) {
        this.amountBorrowed = amountBorrowed;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTenureDays() {
        return tenureDays;
    }

    public void setTenureDays(int tenureDays) {
        this.tenureDays = tenureDays;
    }

    @Override
    public String toString() {
        return "Name: " + this.borrowerName + "amount:" + this.amount + "money";
    }
}
