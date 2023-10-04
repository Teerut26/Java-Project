package cs211.project.models.collections;

import cs211.project.models.ManyToMany;

import java.util.ArrayList;

public class ManyToManyCollection {
    private ArrayList<ManyToMany> manyToManies;

    public ManyToManyCollection() {
        this.manyToManies = new ArrayList<>();
    }

    public void add(ManyToMany manyToMany) {
        if (checkIsExisted(manyToMany)) {
            return;
        }
        this.manyToManies.add(manyToMany);
    }

    public ManyToMany find(ManyToMany manyToMany) {
        for (ManyToMany manyToMany1 : this.manyToManies) {
            if (manyToMany1.getA().equals(manyToMany.getA()) && manyToMany1.getB().equals(manyToMany.getB())) {
                return manyToMany1;
            }
        }
        return null;
    }

    public ArrayList<ManyToMany> findsByA(String primary) {
        ArrayList<ManyToMany> manyToManies = new ArrayList<>();
        for (ManyToMany manyToMany : this.manyToManies) {
            if (manyToMany.getA().equals(primary)) {
                manyToManies.add(manyToMany);
            }
        }
        return manyToManies;
    }

    public ArrayList<ManyToMany> findsByB(String secondary) {
        ArrayList<ManyToMany> manyToManies = new ArrayList<>();
        for (ManyToMany manyToMany : this.manyToManies) {
            if (manyToMany.getB().equals(secondary)) {
                manyToManies.add(manyToMany);
            }
        }
        return manyToManies;
    }

    public boolean checkIsExisted(ManyToMany manyToMany) {
        for (ManyToMany manyToMany1 : this.manyToManies) {
            if (manyToMany1.getA().equals(manyToMany.getA()) && manyToMany1.getB().equals(manyToMany.getB())) {
                return true;
            }
        }
        return false;
    }

    public void remove(ManyToMany manyToMany) {
        this.manyToManies.remove(manyToMany);
    }

    public ArrayList<ManyToMany> getManyToManies() {
        return this.manyToManies;
    }

    public int size() {
        return this.manyToManies.size();
    }


    public void setManyToManies(ArrayList<ManyToMany> manyToManies) {
        this.manyToManies = manyToManies;
    }
}
