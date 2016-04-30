package simpledb;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool which check that the transaction has the appropriate
 * locks to read/write the page.
 */
public class BufferPool {
    /** Bytes per page, excluding header. */
    public static final int PAGE_SIZE = 4096;
    public static final int DEFAULT_PAGES = 100;
    public static final int DEFAULT_POLICY = 0;
    public static final int LRU_POLICY = 1;
    public static final int MRU_POLICY = 2;
       
    int replace_policy = DEFAULT_POLICY;
    int _numhits=0;
    int _nummisses=0;
    
    private int MAX_PAGES;
    private Map<PageId, Page> bufferpool;
    private ArrayList<PageId> useOrder;
    private DbFile dbFile;

    /**
     * Constructor.
     *
     * @param numPages number of pages in this buffer pool
     */
    public BufferPool(int numPages) {
        //IMPLEMENT THIS
    	MAX_PAGES = numPages;
    	bufferpool = new ConcurrentHashMap<PageId, Page>();
    	useOrder = new ArrayList<PageId>();
    }

  
    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, an page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public synchronized Page getPage(TransactionId tid, PageId pid, Permissions perm) throws TransactionAbortedException, DbException, IOException {
    	//IMPLEMENT THIS
    	if(bufferpool.size() == MAX_PAGES){
    		//System.out.println("EVICTION ");
    		evictPage();
    	}
    	if(bufferpool.containsKey(pid)){
    		useOrder.remove(pid);
    		useOrder.add(pid);
    		_numhits++;
    		return bufferpool.get(pid);
    	}
    	_nummisses++;
    	dbFile = Database.getCatalog().getDbFile(pid.tableid());
    	Page page = dbFile.readPage(pid);
    	bufferpool.put(pid, page);
		useOrder.add(pid);
    	return page;
}

    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param pid the ID of the page to unlock
     */
    public synchronized void releasePage(TransactionId tid, PageId pid) {
        // no need to implement this
       
    }

    /**
     * Release all locks associated with a given transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     */
    public synchronized void transactionComplete(TransactionId tid) throws IOException {
       // no need to implement this
     }

    /** Return true if the specified transaction has a lock on the specified page */
    public  synchronized boolean holdsLock(TransactionId tid, PageId p) {
       // no need to implement this
         return false;
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public  synchronized void transactionComplete(TransactionId tid, boolean commit) throws IOException {
       // no need to implement this
    }

    /**
     * Add a tuple to the specified table behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to. May block if
     * the lock cannot be acquired.
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public synchronized void insertTuple(TransactionId tid, int tableId, Tuple t) throws DbException, IOException, TransactionAbortedException {
       // no need to implement this

    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is added to. May block if
     * the lock cannot be acquired.
     *
     * @param tid the transaction adding the tuple.
     * @param t the tuple to add
     */
    public synchronized void deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
    	// no need to implement this

    }

    /**
     * Flush all dirty pages to disk.
     * NB: Be careful using this routine -- it writes dirty data to disk so will
     *     break simpledb if running in NO STEAL mode.
     */
    public synchronized void flushAllPages() throws IOException {
           // no need to implement this
    }

    /** Remove the specific page id from the buffer pool.
        Needed by the recovery manager to ensure that the
        buffer pool doesn't keep a rolled back page in its
        cache.
    */
    public synchronized void discardPage(PageId pid) {
    	// no need to implement this
    }

    /**
     * Flushes a certain page to disk
     * @param pid an ID indicating the page to flush
     */
    private  synchronized void flushPage(PageId pid) throws IOException {
         // no need to implement this
    }

    /** Write all pages of the specified transaction to disk.
     */
    public synchronized  void flushPages(TransactionId tid) throws IOException {
    	// no need to implement this
    }

    /**
     * Discards a page from the buffer pool. Return index of discarded page
     */
    private  synchronized int evictPage() throws DbException {
    	//IMPLEMENT THIS
    	int index = 0;
    	//System.out.println(useOrder);
    	//System.out.println("Replace Policy " + replace_policy);
    	if(replace_policy == LRU_POLICY || replace_policy == DEFAULT_POLICY){
    		//least recently used
    		//should be the first one in the list
    		//System.out.println(useOrder.get(0));
    		bufferpool.remove(useOrder.get(0));
    		useOrder.remove(0);
    		return index;
    	}
    	else if(replace_policy == MRU_POLICY){
    		//most recently used
    		//last one in the list
    		//System.out.println(useOrder.get(useOrder.size()-1));
    		bufferpool.remove(useOrder.get(useOrder.size()-1));
    		useOrder.remove(useOrder.size()-1);
    	}
    	return useOrder.size()-1;
    }

    public int getNumHits(){
    	return _numhits;
    }
    public int getNumMisses(){
    	return _nummisses;
    }
    
    public void setReplacePolicy(int replacement){
    	this.replace_policy=replacement;
    }
}