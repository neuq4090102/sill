package com.elv.frame.model;

/**
 * 脱敏参数控制
 *
 * @author lxh
 * @date 2020-04-10
 */
public class Blur {

    private int fromIdx;
    private int toIdx;
    private int stepSize;//步长
    private double ratio; //比例
    private String mask = "*"; //脱敏符号

    private boolean resetStepSize;

    private Blur() {
    }

    private Blur(Blur.Builder builder) {
        this.setFromIdx(builder.getFromIdx());
        this.setToIdx(builder.getToIdx());
        this.setStepSize(builder.getStepSize());
        this.setRatio(builder.getRatio());
        this.setMask(builder.getMask());
        this.setResetStepSize(builder.isResetStepSize());
    }

    public int getFromIdx() {
        return fromIdx;
    }

    public void setFromIdx(int fromIdx) {
        this.fromIdx = fromIdx;
    }

    public int getToIdx() {
        return toIdx;
    }

    public void setToIdx(int toIdx) {
        this.toIdx = toIdx;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public boolean isResetStepSize() {
        return resetStepSize;
    }

    public void setResetStepSize(boolean resetStepSize) {
        this.resetStepSize = resetStepSize;
    }

    public static Blur.Builder builder() {
        return new Blur.Builder();
    }

    public static class Builder {
        private int fromIdx;
        private int toIdx;
        private int stepSize;
        private double ratio;
        private String mask = "*";
        private boolean resetStepSize = false;

        public int getFromIdx() {
            return fromIdx;
        }

        public Blur.Builder fromIdx(int fromIdx) {
            this.fromIdx = fromIdx;
            return this;
        }

        public int getToIdx() {
            return toIdx;
        }

        public Blur.Builder toIdx(int toIdx) {
            this.toIdx = toIdx;
            return this;
        }

        public int getStepSize() {
            return stepSize;
        }

        public Blur.Builder stepSize(int stepSize) {
            this.stepSize = stepSize;
            this.resetStepSize = true;
            return this;
        }

        public double getRatio() {
            return ratio;
        }

        public Blur.Builder ratio(double ratio) {
            this.ratio = ratio;
            return this;
        }

        public String getMask() {
            return mask;
        }

        public Blur.Builder mask(String mask) {
            this.mask = mask;
            return this;
        }

        public boolean isResetStepSize() {
            return resetStepSize;
        }

        public void setResetStepSize(boolean resetStepSize) {
            this.resetStepSize = resetStepSize;
        }

        public Blur build() {

            return new Blur(this);
        }
    }
}
