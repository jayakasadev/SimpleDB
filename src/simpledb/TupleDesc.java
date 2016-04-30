package simpledb;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {
    private int numFields;
    private Type [] fieldTypes;
    private String [] fieldNames;

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields
     * fields, with the first td1.numFields coming from td1 and the remaining
     * from td2.
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
    	Type mergedTypes[] = new Type[td1.numFields()+td2.numFields()];
    	String mergedNames[] = new String[td1.numFields()+td2.numFields()];
    	for (int i=0; i<td1.numFields(); i++){
    		mergedTypes[i]=td1.getType(i);
	    	mergedNames[i]=td1.getFieldName(i);
    	}
    	for (int j=0; j<td2.numFields(); j++){
    		mergedTypes[td1.numFields()+j]=td2.getType(j);
    		mergedNames[td1.numFields()+j]=td2.getFieldName(j);
    	}	    
    	TupleDesc mergedtuple = new TupleDesc(mergedTypes,mergedNames);
        return mergedtuple;
    }

    /**
     * Constructor.
     * Create a new tuple desc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {

    	this.numFields = typeAr.length;
    	this.fieldTypes = new Type[numFields];
    	this.fieldNames = new String[numFields];

    	for (int i=0; i<this.numFields; i++){
    		fieldTypes[i] = typeAr[i];
            fieldNames[i] = fieldAr[i];
    	}
    }

    /**
     * Constructor.
     * Create a new tuple desc with typeAr.length fields with fields of the
     * specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in
     *        this TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {

    	this.numFields = typeAr.length;
    	this.fieldTypes = new Type[numFields];
    	this.fieldNames = new String[numFields];

    	for (int i=0; i<this.numFields; i++){
    		fieldTypes[i] = typeAr[i];
    		fieldNames[i] = null;
    	}

    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return this.numFields;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
    	if (i >= this.numFields)
    		throw new NoSuchElementException();
    	return this.fieldNames[i];
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int nameToId(String name) throws NoSuchElementException {
    	for (int i=0; i < this.numFields; i++) {
    		if (fieldNames[i] == null){
    			throw new NoSuchElementException();
    		}
    		if (fieldNames[i].equals(name))
    			return i;
    	}
    	throw new NoSuchElementException();
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
    	if (i >= this.numFields)
	    	throw new NoSuchElementException();
        return this.fieldTypes[i];
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
    	int tuplebytesize = 0;
    	for (int i=0; i<this.numFields; i++)
    		tuplebytesize += fieldTypes[i].getLen();
    	return tuplebytesize;
    }

    /**
     * Compares the specified object with this TupleDesc for equality.
     * Two TupleDescs are considered equal if they are the same size and if the
     * n-th type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
    	TupleDesc otd;
		if (o==null)
			return false;
		try{
			otd = (TupleDesc) o;
		} catch (ClassCastException e) {
			return false;
		}
		if (this.getSize()!=otd.getSize())
			return false;
		for (int i=0; i<this.numFields; i++){
			if (this.getType(i)!=otd.getType(i))
				return false;
		} 
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * @return String describing this descriptor.
     */
    public String toString() {
    	String tupledescriptor = new String();
    	for (int i=0; i<this.numFields; i++){
    		tupledescriptor += fieldTypes[i];
    		tupledescriptor += "(";
    		tupledescriptor += fieldNames[i];
    		tupledescriptor += "),";
    	}
        return tupledescriptor;
    }
}
