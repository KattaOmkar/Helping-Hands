package omkar.com.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanGroup {
    private ArrayList<Lender> lenders;
    private String borrowerAddress;
    private String reason;
    private float amount;
    private float amountBorrowed;
    private String borrowerID;
    private String borrowerName;
    private long date;
    private float interest;
    private String phone;
    private int tenureMonths;

    public LoanGroup() {
    }

    public static Map<String, Object> makeTOMap(final LoanGroup loanGroup) {
        if (loanGroup != null) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("borrowerID", loanGroup.getBorrowerID());
            objectMap.put("borrowerName", loanGroup.getBorrowerName());
            objectMap.put("date", String.valueOf(loanGroup.getDate()));
            objectMap.put("borrowerAddress", loanGroup.getBorrowerAddress());
            objectMap.put("reason", loanGroup.getReason());
            objectMap.put("phone", loanGroup.getPhone());
            objectMap.put("amount", loanGroup.getAmount());
            objectMap.put("amountBorrowed", loanGroup.getAmountBorrowed());
            objectMap.put("interest", loanGroup.getInterest());
            objectMap.put("tenureMonths", loanGroup.getTenureMonths());
            return objectMap;
        }
        return null;

    }

    public static LoanGroup makeFromMap(final Map<String, Object> map) {
        Gson gson = new Gson();
//        LoanGroup g = gson.fromJson(map.toString(), LoanGroup.class);
        LoanGroup g = new LoanGroup();
        ArrayList<Lender> lenderArrayList = new ArrayList<>();
//        Log.d("LOAN GRP", map.toString());
//        Log.d("LOAN GRP", g.borrowerId);
//        Log.d("LOAN GRP", g.borrowerName);
//        Log.d("LOAN GRP", g.borrowerAddress);

        if ((map != null) && (!map.isEmpty())) {
            if (!map.get("borrowerID").equals("Borrowerid")) {
                g.setBorrowerID(map.get("borrowerID").toString());
                g.setDate(Long.parseLong(map.get("date").toString()));
                g.setBorrowerName(map.get("borrowerName").toString());
                g.setBorrowerAddress(map.get("borrowerAddress").toString());
                g.setReason(map.get("reason").toString());
                g.setPhone(map.get("phone").toString());
                g.setAmount(Float.parseFloat(map.get("amount").toString()));
                g.setAmountBorrowed(Float.parseFloat(map.get("amountBorrowed").toString()));
                g.setInterest(Float.parseFloat(map.get("interest").toString()));
                g.setTenureMonths(Integer.parseInt(map.get("tenureMonths").toString()));

                Map<String, Object> mapL = (HashMap) map.get("lenders");
                if ((mapL != null) && (!mapL.isEmpty())) {


                    for (String key : mapL.keySet()) {
                        Lender lender = new Gson().fromJson(mapL.get(key).toString(), Lender.class);
                        lenderArrayList.add(lender);
                    }
                }

                g.setLenders(lenderArrayList);


            }
        }
        return g;
    }

    public ArrayList<Lender> getLenders() {
        return ((lenders != null) ? lenders : new ArrayList<Lender>());
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

    public String getBorrowerID() {
        return borrowerID;
    }

    public void setBorrowerID(String borrowerId) {
        this.borrowerID = borrowerId;
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

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    @Override
    public String toString() {
        return "Name: " + this.borrowerName + "amount:" + this.amount + "money";
    }
}
