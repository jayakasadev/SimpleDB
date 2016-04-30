package simpledb;

import java.util.NoSuchElementException;

/** Helper for implementing DbIterators. Handles hasNext()/next() logic and
 *  throwing exceptions if open()/close() are abused. */
public abstract class AbstractDbIterator implements DbIterator {
    //    @Override
    public boolean hasNext() throws DbException, TransactionAbortedException {
    	//System.out.println("AbstractDbIterator hasNext calls readNext");
        if (next == null) 
        	next = readNext();
        //System.out.println("AbstractDbIterator hasNext is next null? " + (next != null));
        //System.out.println(next.getTupleDesc());
        //System.out.println(next);
        return next != null;
    }

    //    @Override
    public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
        if (next == null) {
            next = readNext();
            if (next == null) 
            	throw new NoSuchElementException();
        }

        Tuple result = next;
        //System.out.println("AbstractDbIterator next() " + result);
        next = null;
        return result;
    }

    /** @return the next Tuple in the iterator, null if the iteration is finished. */
    protected abstract Tuple readNext() throws DbException, TransactionAbortedException;

    /** If subclasses override this, they should call super.close(). */
    //    @Override
    public void close() {
        // Ensures that a future call to next() will fail
        next = null;
    }

    private Tuple next = null;
}
