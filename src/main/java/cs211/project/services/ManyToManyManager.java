package cs211.project.services;

import cs211.project.models.ManyToMany;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.services.datasource.ManyToManyFileListDatasource;

public class ManyToManyManager {
    private String file;

    public ManyToManyManager(String file) {
        this.file = file;
    }

    public void add(ManyToMany manyToMany) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        manyToManyCollection.add(manyToMany);
        manyToManyFileListDatasource.writeData(manyToManyCollection);
    }

    public void remove(ManyToMany manyToMany) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        manyToManyCollection.remove(manyToMany);
        manyToManyFileListDatasource.writeData(manyToManyCollection);
    }

    public void removesByA(String primary) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        ManyToManyCollection manyToManyCollection1 = new ManyToManyCollection();
        for (ManyToMany manyToMany : manyToManyCollection.getManyToManies()) {
            if (!manyToMany.getA().equals(primary)) {
                manyToManyCollection1.add(manyToMany);
            }
        }
        manyToManyFileListDatasource.writeData(manyToManyCollection1);
    }

    public void removeByB(String secondary) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        ManyToManyCollection manyToManyCollection1 = new ManyToManyCollection();
        for (ManyToMany manyToMany : manyToManyCollection.getManyToManies()) {
            if (!manyToMany.getB().equals(secondary)) {
                manyToManyCollection1.add(manyToMany);
            }
        }
        manyToManyFileListDatasource.writeData(manyToManyCollection1);
    }

    public ManyToManyCollection findsByA(String primary) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        ManyToManyCollection manyToManyCollection1 = new ManyToManyCollection();
        for (ManyToMany manyToMany : manyToManyCollection.getManyToManies()) {
            if (manyToMany.getA().equals(primary)) {
                manyToManyCollection1.add(manyToMany);
            }
        }
        return manyToManyCollection1;
    }

    public ManyToManyCollection findsByB(String secondary) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        ManyToManyCollection manyToManyCollection1 = new ManyToManyCollection();
        for (ManyToMany manyToMany : manyToManyCollection.getManyToManies()) {
            if (manyToMany.getB().equals(secondary)) {
                manyToManyCollection1.add(manyToMany);
            }
        }
        return manyToManyCollection1;
    }

    public Integer countByA(String A) {
        return this.findsByA(A).getManyToManies().size();
    }

    public Integer countByB(String B) {
        return this.findsByB(B).getManyToManies().size();
    }

    public boolean checkIsExisted(ManyToMany manyToMany) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        for (ManyToMany manyToMany1 : manyToManyCollection.getManyToManies()) {
            if (manyToMany1.getA().equals(manyToMany.getA()) && manyToMany1.getB().equals(manyToMany.getB())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAHaveB (String A, String B) {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        ManyToManyCollection manyToManyCollection = manyToManyFileListDatasource.readData();
        for (ManyToMany manyToMany1 : manyToManyCollection.getManyToManies()) {
            if (manyToMany1.getA().equals(A) && manyToMany1.getB().equals(B)) {
                return true;
            }
        }
        return false;
    }

    public ManyToManyCollection getAll() {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        return manyToManyFileListDatasource.readData();
    }

    public void removeAll() {
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(this.file);
        manyToManyFileListDatasource.writeData(new ManyToManyCollection());
    }

}
