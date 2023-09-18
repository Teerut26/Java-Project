package cs211.project.services;

import cs211.project.models.ManyToMany;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManyToManyManagerTest {

    @Test
    void add() {
    }

    @Test
    void remove() {
        ManyToManyManager manyToManyManager = new ManyToManyManager("_test.csv");
        manyToManyManager.removeAll();

        manyToManyManager.add(new ManyToMany("a", "b"));
        manyToManyManager.add(new ManyToMany("a", "c"));
        manyToManyManager.add(new ManyToMany("a", "d"));
        manyToManyManager.add(new ManyToMany("a", "e"));
        manyToManyManager.add(new ManyToMany("a", "f"));

        manyToManyManager.remove(new ManyToMany("a", "b"));

        assertEquals(4, manyToManyManager.getAll().getManyToManies().size());
    }

    @Test
    void removesByA() {
    }

    @Test
    void removeByB() {
    }

    @Test
    void findsByA() {
    }

    @Test
    void findsByB() {
    }

    @Test
    void checkIsExisted() {
    }

    @Test
    void getAll() {
    }
}