package omkar.com.models;

public class Lender {
    boolean isAmount_lent;
    boolean isAmount_refunded;
    private String userId;
    private String userName;
    private String lender_Name;
    private float amount_Lent;
    private float returnAmount;

    public Lender() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getLender_Name() {
        return lender_Name;
    }

    public void setLender_Name(String lender_Name) {
        this.lender_Name = lender_Name;
    }

    public float getAmount_Lent() {
        return amount_Lent;
    }

    public void setAmount_Lent(float amount_Lent) {
        this.amount_Lent = amount_Lent;
    }

    public float getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(float returnAmount) {
        this.returnAmount = returnAmount;
    }

    public boolean isAmount_lent() {
        return isAmount_lent;
    }

    public void setAmount_lent(boolean amount_lent) {
        isAmount_lent = amount_lent;
    }

    public boolean isAmount_refunded() {
        return isAmount_refunded;
    }

    public void setAmount_refunded(boolean amount_refunded) {
        isAmount_refunded = amount_refunded;
    }
}
