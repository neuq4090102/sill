package com.elv.core.tool.text.censor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elv.core.tool.db.jdbc.BasicQuery;
import com.elv.core.tool.db.model.Authorization;
import com.elv.core.tool.text.model.BaiduAipResult;
import com.elv.core.tool.text.model.BaiduDataResult;
import com.elv.core.tool.text.model.BaiduTokenResult;
import com.elv.core.util.HttpUtil;
import com.elv.core.util.JsonUtil;
import com.elv.core.util.StrUtil;

/**
 * 百度工具
 *
 * <ol>
 *  <li>文本审核</li>
 *  <li>图片审核</li>
 * </ol>
 *
 * @author lxh
 * @since 2020-07-29
 */
public class BaiduUtil {

    private static final Logger logger;
    private static final String TOKEN_URL;
    private static final String TEXT_URL;
    private static final String IMAGE_URL;
    // private static final String API_KEY; // 注册账户申请
    // private static final String SECRET_KEY; // 注册账户申请

    static {
        logger = LoggerFactory.getLogger(BaiduUtil.class);
        TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
        TEXT_URL = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        IMAGE_URL = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
    }

    /**
     * 文本审核
     *
     * @param text 文本内容
     * @return boolean
     * @since 2020-07-31
     */
    public static boolean checkText(String text) {
        BaiduAipResult aipResult = Verify.checkText(text);
        if (aipResult == null) {
            return true;
        }

        ConclusionEnum conclusion = ConclusionEnum.itemOf(aipResult.getConclusion());
        if (conclusion != ConclusionEnum.INVALID) {
            return true;
        }
        return false;
    }

    /**
     * 图片审核
     *
     * @param imageUrlOrBase64Str 图片URL或者是base64字节码
     * @return boolean
     * @since 2020-07-31
     */
    public static boolean checkImage(String imageUrlOrBase64Str) {
        if (imageUrlOrBase64Str == null) {
            return false;
        }

        BaiduAipResult aipResult = null;
        if (imageUrlOrBase64Str.startsWith("http")) {
            aipResult = Verify.checkImageByUrl(imageUrlOrBase64Str);
        } else {
            aipResult = Verify.checkImageByBase64(imageUrlOrBase64Str);
        }
        if (aipResult == null) {
            return true;
        }
        ConclusionEnum conclusion = ConclusionEnum.itemOf(aipResult.getConclusion());
        if (conclusion != ConclusionEnum.INVALID) {
            return true;
        }
        return false;
    }

    /**
     * 文本审核 & 图像审核
     */
    static class Verify {

        public static String fetchToken() {
            // TODO 从DB或者redis中获取token，如果没有则请求接口并缓存
            String url = TOKEN_URL + "?grant_type=client_credentials&client_id=%s&client_secret=%s";
            String result = HttpUtil.get(String.format(url, getApiKey(), getSecretKey()));
            BaiduTokenResult baiduToken = JsonUtil.toObject(result, BaiduTokenResult.class);
            if (baiduToken != null && StrUtil.isNotBlank(baiduToken.getAccess_token())) {
                // TODO insert to db or cache into redis.
                return baiduToken.getAccess_token();
            }

            logger.warn("BaiduUtil, token is empty.");

            return "";
        }

        private static String getApiKey() {
            return authorization().getApiKey();
        }

        private static String getSecretKey() {
            return authorization().getAppSecret();
        }

        private static Authorization authorization() {
            return BasicQuery.authorization();
        }

        /**
         * 文本审核
         * <p>
         * 长度限制：长度<2w字节（约6666个汉字）
         *
         * @param text
         * @return
         */
        public static BaiduAipResult checkText(String text) {
            int limitLength = 6000;
            String token = fetchToken();
            if (StrUtil.isBlank(token)) {
                return null;
            }
            int loops = (int) Math.ceil(text.length() / (limitLength * 1.0d)); // 向上取整
            if (loops == 1) {
                String url = TEXT_URL + "?access_token=%s&text=%s";
                String result = HttpUtil.get(String.format(url, token, text));
                return JsonUtil.toObject(result, BaiduAipResult.class);
            }

            BaiduAipResult result = null;
            for (int i = 0; i < loops; i++) {
                String url = TEXT_URL + "?access_token=%s&text=%s";
                String substring = text.substring(limitLength * i, Math.min(limitLength * (i + 1), text.length()));
                String request = HttpUtil.get(String.format(url, token, substring));
                BaiduAipResult aipResult = JsonUtil.toObject(request, BaiduAipResult.class);

                ConclusionEnum conclusion = ConclusionEnum.itemOf(aipResult.getConclusion());
                if (conclusion == ConclusionEnum.INVALID) {
                    List<BaiduDataResult> data = Optional.ofNullable(aipResult.getData()).orElse(new ArrayList<>());
                    if (result == null) {
                        result = aipResult;
                        result.setData(data);
                    } else {
                        result.getData().addAll(data);
                    }
                }
            }

            return result;
        }

        /**
         * 根据图片URL审核图片
         * <p>
         * 图片格式限制：PNG、JPG、JPEG、BMP、GIF（仅对首帧进行审核）、Webp、TIFF
         * 大小限制：要求base64后大于等于5kb，小于等于4M，最短边大于等于128像素，小于等于4096像素
         *
         * @param imageUrl
         * @return
         */
        public static BaiduAipResult checkImageByUrl(String imageUrl) {
            String token = fetchToken();
            if (StrUtil.isBlank(token)) {
                return null;
            }
            String url = IMAGE_URL + "?access_token=%s&imgUrl=%s";
            String result = HttpUtil.get(String.format(url, token, imageUrl));
            return JsonUtil.toObject(result, BaiduAipResult.class);
        }

        /**
         * 根据图片base64字节码审核图片
         *
         * @param imageBase64
         * @return
         */
        public static BaiduAipResult checkImageByBase64(String imageBase64) {
            String token = fetchToken();
            if (StrUtil.isBlank(token)) {
                return null;
            }
            String url = IMAGE_URL + "?access_token=%s&image=%s";
            String result = HttpUtil.get(String.format(url, token, imageBase64));
            return JsonUtil.toObject(result, BaiduAipResult.class);
        }
    }

    public enum ConclusionEnum {
        VALID("合规"), //
        INVALID("不合规"), //
        SUSPECTED("疑似"), //
        FAILED("审核失败"), //
        ;

        private static final Map<String, ConclusionEnum> map;

        static {
            map = Arrays.stream(ConclusionEnum.values())
                    .collect(Collectors.toMap(ConclusionEnum::getValue, item -> item));
        }

        private final String value;

        ConclusionEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ConclusionEnum itemOf(String value) {
            return map.get(value);
        }
    }

    public static void main(String[] args) {
        String text = "百度是傻逼不啦";
        // String text = "傻逼";
        // String text = "我是中国人";

        // boolean valid = checkText(text);
        // System.out.println(valid);

        BaiduAipResult audit = Verify.checkText(text);

        if (audit != null) {
            System.out.println(audit.getConclusion());
            ConclusionEnum conclusion = ConclusionEnum.itemOf(audit.getConclusion());
            if (conclusion == ConclusionEnum.INVALID) {
                System.out.println(audit.getData().get(0).getMsg());
                System.out.println(audit.getData().get(0).getHits().get(0).getWords());
            }
        }

        // Thread.sleep(200); // 规避QPS限制
        //
        // String imageUrl = "https://cdn.pixabay.com/photo/2020/07/19/21/50/forest-5421362_960_720.jpg";
        // System.out.println(checkImage(imageUrl));

    }
}
