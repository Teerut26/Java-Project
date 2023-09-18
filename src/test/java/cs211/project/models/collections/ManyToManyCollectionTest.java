package cs211.project.models.collections;

import cs211.project.models.ManyToMany;
import cs211.project.services.ManyToManyManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManyToManyCollectionTest {

    @Test
    void add() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        manyToManyCollection.add(new ManyToMany("a", "b"));
        manyToManyCollection.add(new ManyToMany("a", "c"));

        assertEquals(2, manyToManyCollection.getManyToManies().size());
    }

    @Test
    void findsByA() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        manyToManyCollection.add(new ManyToMany("a", "b"));
        manyToManyCollection.add(new ManyToMany("a", "c"));
        manyToManyCollection.add(new ManyToMany("a", "d"));
        manyToManyCollection.add(new ManyToMany("a", "e"));

        assertEquals(4, manyToManyCollection.findsByA("a").size());
    }

    @Test
    void findsByB() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        manyToManyCollection.add(new ManyToMany("a", "b"));
        manyToManyCollection.add(new ManyToMany("c", "b"));
        manyToManyCollection.add(new ManyToMany("d", "b"));
        manyToManyCollection.add(new ManyToMany("e", "b"));

        assertEquals(4, manyToManyCollection.findsByB("b").size());
    }

    @Test
    void checkIsExisted() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        manyToManyCollection.add(new ManyToMany("a", "b"));
        manyToManyCollection.add(new ManyToMany("c", "b"));
        manyToManyCollection.add(new ManyToMany("d", "b"));
        manyToManyCollection.add(new ManyToMany("e", "b"));

        assertTrue(manyToManyCollection.checkIsExisted(new ManyToMany("a", "b")));
    }

    @Test
    void remove() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        manyToManyCollection.add(new ManyToMany("a", "b"));
        manyToManyCollection.add(new ManyToMany("c", "b"));
        manyToManyCollection.add(new ManyToMany("d", "b"));
        manyToManyCollection.add(new ManyToMany("e", "b"));

        manyToManyCollection.remove(new ManyToMany("a", "b"));

        assertEquals(3, manyToManyCollection.getManyToManies().size());
    }

    @Test
    void getManyToManies() {
    }

    @Test
    void setManyToManies() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void testFindsByA() {
    }

    @Test
    void testFindsByB() {
    }

    @Test
    void testCheckIsExisted() {
    }

    @Test
    void testRemove() {
    }
}