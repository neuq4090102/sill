package com.elv.traning.sensitiveWord;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * @author lxh
 * @see "NLP分词"
 * @since 2020-07-29
 */
public class NlpUtil {

    public static void main(String[] args) {
        // String str = "He expressed hope that the institution can help enhance connectivity, promote green development and boost technological advances by constantly updating its development philosophy, business model and institutional governance, and by providing flexible and diverse development financing products.";
        String str = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] words = tokenizer.tokenize(str);
        for (String word : words) {
            // System.out.println(word);
        }

        words = WhitespaceTokenizer.INSTANCE.tokenize(str);
        for (String word : words) {
            System.out.println(word);
        }

        // TokenizerModel model = new TokenizerModel();

    }
}
