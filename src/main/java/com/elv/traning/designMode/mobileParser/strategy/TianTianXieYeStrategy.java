package com.elv.traning.designMode.mobileParser.strategy;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.elv.traning.designMode.mobileParser.MobileResult;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneNumberDesc;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

/**
 * 网站-全国手机查询
 *
 * @author lxh
 * @since 2020-07-06
 */
public class TianTianXieYeStrategy implements IStrategy {

    private String URI = "http://www.tiantianxieye.com/nub/%s.html";

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        try {
            System.out.println("6666");
            google(mobile);
            Document doc = Jsoup.connect(String.format(URI, mobile)).timeout(5000).get();
            Elements elements = doc.body().select(" div[class=mU] > div[class=mi]"); // 直接定位到归属地信息
            return this.confirm(elements);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private MobileResult confirm(Elements elements) {
        // System.out.println(elements);
        // mockParse();
        // mock();

        if (elements.isEmpty()) {
            return null;
        }
        for (Element element : elements) {
            System.out.println(element.text());
        }

        return null;
    }

    private void google(String mobile) {

        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(86);
        pn.setNationalNumber(Long.parseLong(mobile));

        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
        String desc = geocoder.getDescriptionForNumber(pn, Locale.CHINA);
        System.out.println(desc);
    }

    private void mockParse() {
        try (final WebClient webClient = new WebClient()) {

            // Get the first page
            // final HtmlPage page = webClient.getPage("http://www.tiantianxieye.com/");
            final HtmlPage page = webClient.getPage("https://www.showji.com/search.htm");

            List<HtmlForm> forms = page.getForms();
            for (HtmlForm form : forms) {
                System.out.println(form.getLocalName());
            }

            // Get the form that we are dealing with and within that form,
            // find the submit button and the field that we want to change.
            // final HtmlForm form = page.getFormByName("myform");
            // final HtmlForm form = page.getFormByName("myform");

            // final HtmlSubmitInput button = form.getInputByName("submitbutton");
            // final HtmlSubmitInput button = form.getInputByName("submitbutton");
            // final HtmlTextInput textField = form.getInputByName("userid");

            // Change the value of the text field
            // textField.type("root");

            // Now submit the form by clicking the button and get back the second page.
            // final HtmlPage page2 = button.click();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mock() {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        HtmlPage page = null;
        try {
            page = webClient.getPage("https://www.showji.com/search.htm");//尝试加载上面图片例子给出的网页
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }

        if (page == null) {
            return;
        }

        try {
            HtmlInput input = page.getHtmlElementById("m");
            input.setDefaultValue("18584838281");
            page = page.getHtmlElementById("btnQuery").click();

            System.out.println(page.asXml());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // final HtmlForm form = page.getForms().get(0);
        //
        // final HtmlSubmitInput button = form.getInputByValue("查 询");
        // final HtmlTextInput textField = form.getInputByName("m");
        // textField.setValueAttribute("18584838281");
        // textField.setText("18584838281");
        //
        // try {
        //     final HtmlPage page2 = button.click();
        //     System.out.println(page2.asXml());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }
}
