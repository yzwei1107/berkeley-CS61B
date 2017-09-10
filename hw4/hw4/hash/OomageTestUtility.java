package hw4.hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        Map<Integer, Integer> bucketDistribution = new HashMap<>();
        for (Oomage oomage : oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            if (bucketDistribution.containsKey(bucketNum)) {
                int bucketItemCount = bucketDistribution.get(bucketNum);
                bucketDistribution.put(bucketNum, ++bucketItemCount);
            } else {
                bucketDistribution.put(bucketNum, 1);
            }
        }

        for (Integer bucketNum : bucketDistribution.keySet()) {
            boolean hasAGoodItemCount = bucketDistribution.get(bucketNum) >= oomages.size() / 50;
            hasAGoodItemCount = hasAGoodItemCount
                    && bucketDistribution.get(bucketNum) <= oomages.size() / 2.5;
            if (!hasAGoodItemCount) {
                return false;
            }
        }
        return true;
    }
}
