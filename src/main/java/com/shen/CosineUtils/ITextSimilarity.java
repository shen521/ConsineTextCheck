package com.shen.CosineUtils;
import java.util.List;

/**
 * 继承ISimilarity
 * 定义以Word为泛型的函数接口
 */
public interface ITextSimilarity extends ISimilarity {

    double getSimilarity(List<Word> var1, List<Word> var2);

}

