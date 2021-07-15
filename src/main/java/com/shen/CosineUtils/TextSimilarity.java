package com.shen.CosineUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 补充以下：
 * Java在变量赋值的时候，其中   float、double、long 数据类型变量，
 * 需要在赋值直接量后面分别添加 f或F、 d或D、  l或L 尾缀来说明。
 */
public abstract class TextSimilarity implements ITextSimilarity {

    public TextSimilarity() {
    }

    /**
     * 通过isBlank判断，得到相似度
     * @param text1
     * @param text2
     * @return
     */
    public double getSimilarity(String text1, String text2) {

        if (StringUtil.isBlank(text1) && StringUtil.isBlank(text2)) {
            return 1.0D;
        } else if (!StringUtil.isBlank(text1) && !StringUtil.isBlank(text2))
        {
            if (text1.equalsIgnoreCase(text2)) {
                return 1.0D;
            } else {
                List<Word> words1 = Tokenizer.segment(text1);
                List<Word> words2 = Tokenizer.segment(text2);
                return this.getSimilarity(words1, words2);
            }

        } else {
            return 0.0D;
        }
    }

    /**
     * 比较两个单词的相似度
     * @param words1
     * @param words2
     * @return
     */
    public double getSimilarity(List<Word> words1, List<Word> words2) {
        if (StringUtil.isBlank(words1) && StringUtil.isBlank(words2))
        {
            return 1.0D;
        } else if (!StringUtil.isBlank(words1) && !StringUtil.isBlank(words2))
        {

            double score = this.getSimilarityImpl(words1, words2);

            score = (double)((int)(score * 1000000.0D + 0.5D)) / 1000000.0D;
            return score;

        } else {

            return 0.0D;
        }
    }

    //定义抽象方法
    protected abstract double getSimilarityImpl(List<Word> var1, List<Word> var2);

    /**
     * 得到两个words的出现频率
     * @param words1
     * @param words2
     */
    protected void taggingWeightByFrequency(List<Word> words1, List<Word> words2) {
        if (((Word)words1.get(0)).getWeight() == null && ((Word)words2.get(0)).getWeight() == null)
        {
            Map<String, AtomicInteger> frequency1 = this.getFrequency(words1);
            Map<String, AtomicInteger> frequency2 = this.getFrequency(words2);
            words1.parallelStream().forEach((word) ->
                 { word.setWeight(((AtomicInteger)frequency1.get(word.getName())).floatValue()); });

            words2.parallelStream().forEach((word) ->
                { word.setWeight(((AtomicInteger)frequency2.get(word.getName())).floatValue()); });
        }
    }

    /**
     * 得到单个words的复现率
     * @param words
     * @return
     */
    private Map<String, AtomicInteger> getFrequency(List<Word> words) {
        Map<String, AtomicInteger> freq = new HashMap();
        words.forEach((i) -> {
            ((AtomicInteger)freq.computeIfAbsent(i.getName(), (k) -> {
                return new AtomicInteger();
            })).incrementAndGet();
        });
        return freq;
    }

    /**
     * 存储单词的权重weightMap
     * @param words
     * @return
     */
    protected Map<String, Float> getFastSearchMap(List<Word> words) {
        Map<String, Float> weightMap = new ConcurrentHashMap();
        if (words == null) {
            return weightMap;
        } else {
            words.parallelStream().forEach((i) -> {
                if (i.getWeight() != null) {
                    weightMap.put(i.getName(), i.getWeight());
                }

            });
            return weightMap;
        }
    }
}

