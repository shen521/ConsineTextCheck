package com.shen.CosineUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Tokenizer {

    public Tokenizer() {
    }

    /**
     * 句子标识解析结果
     * @param sentence
     * @return
     */
    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList();
        List<Term> termList = HanLP.segment(sentence);
        results.addAll((Collection)termList.stream().map((term) -> {
            return new Word(term.word, term.nature.name());
        }).collect(Collectors.toList()));

        return results;
    }
}

