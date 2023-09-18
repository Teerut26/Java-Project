package cs211.project.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        ManyToMany that = (ManyToMany) o;
        return this.A.equals(that.A) && this.B.equals(that.B);
    }

    public void setB(String b) {
        B = b;
    }
}
