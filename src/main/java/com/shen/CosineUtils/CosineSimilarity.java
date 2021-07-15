package com.shen.CosineUtils;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity extends TextSimilarity {
    public CosineSimilarity() {
    }

    /**
     * 两个words相似度 抽象方法的实现
     * @param words1
     * @param words2
     * @return
     */
    public double getSimilarityImpl(List<Word> words1, List<Word> words2) {
        this.taggingWeightByFrequency(words1, words2);
        Map<String, Float> weightMap1 = this.getFastSearchMap(words1);
        Map<String, Float> weightMap2 = this.getFastSearchMap(words2);
        Set<Word> words = new HashSet();
        words.addAll(words1);
        words.addAll(words2);
        AtomicFloat ab = new AtomicFloat();
        AtomicFloat aa = new AtomicFloat();
        AtomicFloat bb = new AtomicFloat();
        words.parallelStream().forEach((word) -> {
            Float x1 = (Float)weightMap1.get(word.getName());
            Float x2 = (Float)weightMap2.get(word.getName());
            float oneOfTheDimension;
            if (x1 != null && x2 != null) {
                oneOfTheDimension = x1 * x2;
                ab.addAndGet(oneOfTheDimension);
            }

            if (x1 != null) {
                oneOfTheDimension = x1 * x1;
                aa.addAndGet(oneOfTheDimension);
            }

            if (x2 != null) {
                oneOfTheDimension = x2 * x2;
                bb.addAndGet(oneOfTheDimension);
            }

        });
        //使用BigDecimal的构造方法或者静态方法的valueOf()方法把基本类型的变量构建成BigDecimal对象。
        double aaa = Math.sqrt(aa.doubleValue());
        double bbb = Math.sqrt(bb.doubleValue());
        BigDecimal aabb = BigDecimal.valueOf(aaa).multiply(BigDecimal.valueOf(bbb));
        double cos = BigDecimal.valueOf((double)ab.get()).divide(aabb, 9, 4).doubleValue();
        return cos;
    }
}
