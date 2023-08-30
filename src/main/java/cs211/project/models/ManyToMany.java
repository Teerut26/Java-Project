package cs211.project.models;

public class ManyToMany {
    private String A;
    private String B;

    public ManyToMany(String A, String B) {
        this.A = A;
        this.B = B;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public void setA(String a) {
        A = a;
    }

    public void setB(String b) {
        B = b;
    }
}
