package com.elv.core.constant;

/**
 * @author lxh
 * @since 2020-03-23
 */
public class FormEnum {

    public enum DateForm {
        DATE_TIME("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"), //
        DATE("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}"), //
        TIME("HH:mm:ss", "\\d{2}:\\d{2}:\\d{2}"), //
        YEAR_MONTH("yyyy-MM", "\\d{4}-\\d{2}"), //
        ;

        private final String pattern;
        private final String regex;

        DateForm(String pattern, String regex) {
            this.pattern = pattern;
            this.regex = regex;
        }

        public String getPattern() {
            return pattern;
        }

        public String getRegex() {
            return regex;
        }

        public static boolean hasDate(DateForm dateForm) {
            return DATE == dateForm || DATE_TIME == dateForm;
        }

        public static boolean hasTime(DateForm dateForm) {
            return TIME == dateForm || DATE_TIME == dateForm;
        }
    }
}
