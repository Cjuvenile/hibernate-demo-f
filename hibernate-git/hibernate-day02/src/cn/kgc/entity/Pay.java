package cn.kgc.entity;

public class Pay {
    private int monthlyPay;
    private int yearPay;



    private Worker worker;
    public int getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(int monthlyPay) {
        this.monthlyPay = monthlyPay;
    }

    public int getYearPay() {
        return yearPay;
    }

    public void setYearPay(int yearPay) {
        this.yearPay = yearPay;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public String toString() {
        return "Pay{" +
                "monthlyPay=" + monthlyPay +
                ", yearPay=" + yearPay +
                ", worker=" + worker +
                '}';
    }
}
