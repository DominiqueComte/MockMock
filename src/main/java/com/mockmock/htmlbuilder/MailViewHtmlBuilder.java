package com.mockmock.htmlbuilder;

import com.mockmock.Settings;
import com.mockmock.mail.MockMail;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailViewHtmlBuilder implements HtmlBuilder {
    private Settings settings;
    private MailViewHeadersHtmlBuilder headersBuilder;
    private AddressesHtmlBuilder addressesHtmlBuilder;

    private MockMail mockMail;

    public void setMockMail(MockMail mockMail) {
        this.mockMail = mockMail;
    }

    public String build() {
        headersBuilder.setMockMail(mockMail);

        addressesHtmlBuilder.setMockMail(mockMail);

        String subjectOutput;
        if (mockMail.getSubject() == null) {
            subjectOutput = "<em>No subject given</em>";
        } else {
            subjectOutput = StringEscapeUtils.escapeHtml(mockMail.getSubject());
        }

        subjectOutput += " <small class=\"deleteLink\"><a href=\"/delete/" + mockMail.getId() + "\">Delete</a></small>";

        String output = "<div class=\"container content\">\n";

        output += "<h2>" + subjectOutput + "</h2>\n" + "  <div class=\"row\">\n";

        output += "    <div class=\"span10\" name=\"addresses\">\n" + "       <h3>Addresses</h3>\n" + "       "
                + addressesHtmlBuilder.build() + "    </div>\n";

        if (mockMail.getBody() != null) {
            output += "    <div class=\"span10\" name=\"bodyPlainText\">\n" + "       <h3>Plain text body</h3>\n"
                    + "       <div class=\"well\">" + StringEscapeUtils.escapeHtml(mockMail.getBody()) + "</div>\n"
                    + "    </div>\n";
        }

        if (mockMail.getBodyHtml() != null) {
            // also show a parsed version in div
            output += " <div class=\"span10\" name=\"bodyHTML_Formatted\">\n" + " <h3>HTML body formatted <a href=\"/view/html/"+mockMail.getId()+"\">&lt;html-view&gt;</a></h3>\n"
                    + " <div class=\"well\">" + mockMail.getBodyHtml() + "</div>\n" 
                    + " </div>\n";

            output += "<script>\n"
                    + "  function doForward(id, email){\n"
                    + "    var forwardServer = $('#forwardServer').val();\n"
                    + "    var forwardServerDomain = $('#forwardServerDomain').val();\n"
                    + "    var forwardServerUserName = $('#forwardServerUserName').val();\n"
                    + "    var forwardServerPassword = $('#forwardServerPassword').val();\n"
                    + "    var forwardEmailAdress = $('#forwardEmailAdress').val();\n"                    

                    + "    if (forwardServer == '') {alert('forwardServer is required!'); return;}\n"
                    + "    if (forwardServerDomain == '') {alert('forwardServerDomain is required!'); return;}\n"
                    + "    if (forwardEmailAdress == '') {alert('forwardEmailAdress is required!'); return;}\n"

                    + "    var url = document.location.origin + '/forward/' + id + '/' + forwardServer + '/' + forwardServerDomain + '/' + forwardServerUserName + '/' + forwardServerPassword + '/' + forwardEmailAdress;\n"
                    + "    document.location = url;\n"
                    + "  }\n"
                    + "</script>\n"
                    + "<div class=\"span10\">\n"
                    + " <h3>Forward server params</h3>\n"
                    + " <table>\n"
                    + "   <tr><td>Server</td><td><input type=\"text\" id=\"forwardServer\" value=\""+this.settings.getForwardServer()+"\" /></td><td>&nbsp;</td><td>Domain</td><td><input type=\"text\" id=\"forwardServerDomain\" value=\""+this.settings.getForwardServerDomain()+"\" /></td></tr>\n"
                    + "   <tr><td>UserName</td><td><input type=\"text\" id=\"forwardServerUserName\" value=\""+this.settings.getForwardServerUserName()+"\" /></td><td>&nbsp;</td><td>Password</td><td><input type=\"password\" id=\"forwardServerPassword\" value=\""+this.settings.getForwardServerPassword()+"\" /></td></tr>\n"
                    + "   <tr><td>Forward mail to</td><td><input type=\"text\" id=\"forwardEmailAdress\" value=\""+this.settings.getForwardEmailAdress()+"\" /></td><td>&nbsp;</td><td>&nbsp;</td><td><a href=\"#\" onclick=\"doForward('" + mockMail.getId() +"')\">Forward email</a></td></tr>\n"
                    + " </table>\n "
                    + "</div>";

            // also show a parsed version in an iframe
            output += " <div class=\"span10\" name=\"iFrame\">\n" + " <h3>HTML body formatted in IFrame</h3>\n"
                    + " <iframe class=\"well\" src=\"/view/html/" + mockMail.getId()
                    + "\" style=\"width: 780px; height: 700px; overflow: scroll;\" style=\"\" name=\"bodyHTML_iFrame\">\n"
                    + " </iframe>\n" 
                    + " </div>";

            output += "    <div class=\"span10\" name=\"headers\">\n" + "       <h3>Mail headers</h3>\n" + "       "
                    + headersBuilder.build() + "    </div>\n";
    
            // also show html in a div with a Copy2Clipboard button
            output += " <div class=\"span10\" name=\"bodyHTML_Unformatted\">\n" + " <h3>HTML unformatted</h3>\n"
                    + " <button class=\"btn\" data-clipboard-target=\"#html2copy\">Copy2Clipboard</button>\n "
                    + " <div class=\"well\" id=\"html2copy\">" + StringEscapeUtils.escapeHtml(mockMail.getBodyHtml())
                    + "</div>\n" 
                    + " </div>\n";
        }

        // just output the raw mail so we're sure everything is on the screen
        if (mockMail.getRawMail() != null) {
            // output complete raw mail
            output += "    <div class=\"span10\" name=\"rawOutput\">\n" + "       <h3>Complete raw mail output</h3>\n"
                    + "       <div class=\"well\">" + StringEscapeUtils.escapeHtml(mockMail.getRawMail()) + "</div>\n"
                    + "    </div>\n";
        }

        output += "  </div>\n";

        output += "</div>\n";

        return output;
    }

    @Autowired
    public void setMailViewHeadersHtmlBuilder(MailViewHeadersHtmlBuilder mailViewHeadersHtmlBuilder) {
        this.headersBuilder = mailViewHeadersHtmlBuilder;
    }

    @Autowired
    public void setAddressesHtmlBuilder(AddressesHtmlBuilder addressesHtmlBuilder) {
        this.addressesHtmlBuilder = addressesHtmlBuilder;
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

}