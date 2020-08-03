package com.elv.core.tool.text.model;

import java.util.List;

/**
 * @author lxh
 * @since 2020-08-03
 */
public class BaiduDataResult {
    private String msg; // 不合规项描述信息
    private String conclusion; // 审核结果，可取值：合规、不合规、疑似、审核失败
    private String conclusionType; // 请求唯一id
    private String type; // 审核主类型，11：百度官方违禁词库、12：文本反作弊、13:自定义文本黑名单、14:自定义文本白名单

    /**
     * 审核子类型，此字段需参照type主类型字段决定其含义：
     * 当type=11时subType取值含义：
     * 0:百度官方默认违禁词库
     * 当type=12时subType取值含义：
     * 0:低质灌水、1:暴恐违禁、2:文本色情、3:政治敏感、4:恶意推广、5:低俗辱骂
     * 当type=13时subType取值含义：
     * 0:自定义文本黑名单
     * 当type=14时subType取值含义：
     * 0:自定义文本白名单
     */
    private String subType;
    private List<BaiduHitResult> hits;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getConclusionType() {
        return conclusionType;
    }

    public void setConclusionType(String conclusionType) {
        this.conclusionType = conclusionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public List<BaiduHitResult> getHits() {
        return hits;
    }

    public void setHits(List<BaiduHitResult> hits) {
        this.hits = hits;
    }
}
