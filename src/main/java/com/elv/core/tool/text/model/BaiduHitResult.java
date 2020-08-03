package com.elv.core.tool.text.model;

import java.util.List;

/**
 * @author lxh
 * @since 2020-08-03
 */
public class BaiduHitResult {

    private String probability; // 相似度
    private String datasetName; // 所属数据集名称
    private List<String> words; // 违规文本关键字

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
