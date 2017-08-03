package secondarySort;

/**
 * This class sorts the Map output keys. You'll set the SortComparatorClass of the Job to this
 */

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class KeyComparator extends WritableComparator {
    protected  KeyComparator(){
        super(TextIntPair.class, true);
    }

    @Override
    public int compare(WritableComparable w1, WritableComparable w2){

      TextIntPair p1 = (TextIntPair) w1;
        TextIntPair p2 = (TextIntPair) w2;

        int cmp = p1.getFirst().compareTo(p2.getFirst());
        if (cmp!=0){
            return cmp;
        }
       return p1.getSecond().compareTo(p2.getSecond());


    }

}
