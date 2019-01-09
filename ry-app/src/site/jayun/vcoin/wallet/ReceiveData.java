package site.jayun.vcoin.wallet;

import java.util.List;

public class ReceiveData {
    private String address;
    private String account;
    private double amount;
    private int confirmations;
    private String label;
    private List<String> txids;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getTxids() {
        return txids;
    }

    public void setTxids(List<String> txids) {
        this.txids = txids;
    }
}
