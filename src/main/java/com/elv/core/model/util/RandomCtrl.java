package com.elv.core.model.util;

/**
 * @author lxh
 * @since 2020-06-10
 */
public final class RandomCtrl {

    private static final int DEFAULT_LENGTH = 6;

    private int length;
    private boolean lowerCaseLetter;
    private boolean upperCaseLetter;
    private boolean digit;

    private RandomCtrl() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isLowerCaseLetter() {
        return lowerCaseLetter;
    }

    public void setLowerCaseLetter(boolean lowerCaseLetter) {
        this.lowerCaseLetter = lowerCaseLetter;
    }

    public boolean isUpperCaseLetter() {
        return upperCaseLetter;
    }

    public void setUpperCaseLetter(boolean upperCaseLetter) {
        this.upperCaseLetter = upperCaseLetter;
    }

    public boolean isDigit() {
        return digit;
    }

    public void setDigit(boolean digit) {
        this.digit = digit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int length = DEFAULT_LENGTH;
        private boolean lowerCaseLetter = true;
        private boolean upperCaseLetter = true;
        private boolean digit = true;

        public Builder length(int length) {
            if (length <= 0) {
                length = DEFAULT_LENGTH;
            }
            this.length = length;
            return this;
        }

        public Builder lowerCaseLetter(boolean lowerCaseLetter) {
            this.lowerCaseLetter = lowerCaseLetter;
            return this;
        }

        public Builder upperCaseLetter(boolean upperCaseLetter) {
            this.upperCaseLetter = upperCaseLetter;
            return this;
        }

        public Builder digit(boolean digit) {
            this.digit = digit;
            return this;
        }

        public Builder letter(boolean letter) {
            this.lowerCaseLetter = letter;
            this.upperCaseLetter = letter;
            return this;
        }

        public Builder onlyDigit() {
            this.digit = true;
            this.lowerCaseLetter = false;
            this.upperCaseLetter = false;
            return this;
        }

        public RandomCtrl build() {
            RandomCtrl randomCtrl = new RandomCtrl();
            randomCtrl.setLength(length);
            randomCtrl.setLowerCaseLetter(this.lowerCaseLetter);
            randomCtrl.setUpperCaseLetter(this.upperCaseLetter);
            randomCtrl.setDigit(this.digit);
            return randomCtrl;
        }
    }

}
