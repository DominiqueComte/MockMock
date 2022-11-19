package com.mockmock;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class Settings {
    /**
     * Whether to show console output when receiving email.
     */
    private boolean showEmailInConsole = false;

    /**
     * The default port where MockMock will run on
     */
    private int smtpPort = 25;

    /**
     * The default port for the http server
     */
    private int httpPort = 8282;

    /**
     * The default port for the web api
     */
    private int webApiPort = 8283;

    /**
     * The maximum size the mail queue may be
     */
    private int maxMailQueueSize = 1000;

    /**
     * A set of "From" email addresses to filter
     */
    private Set<String> filterFromEmailAddresses = new HashSet<>();

    /**
     * A set of "To" email addresses to filter
     */
    private Set<String> filterToEmailAddresses = new HashSet<>();

    /**
     * Path to the static folder containing the images, css and js
     */
    private String staticFolderPath;

    public boolean getShowEmailInConsole() {
        return showEmailInConsole;
    }

    public void setShowEmailInConsole(boolean showEmailInConsole) {
        this.showEmailInConsole = showEmailInConsole;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getWebApiPort() {
        return webApiPort;
    }

    public void setWebApiPort(int webApiPort) {
        this.webApiPort = webApiPort;
    }

    public int getMaxMailQueueSize() {
        return maxMailQueueSize;
    }

    public void setMaxMailQueueSize(int maxMailQueueSize) {
        this.maxMailQueueSize = maxMailQueueSize;
    }

    public Set<String> getFilterFromEmailAddresses() {
        return filterFromEmailAddresses;
    }

    public void setFilterFromEmailAddresses(Set<String> filterFromEmailAddresses) {
        this.filterFromEmailAddresses = filterFromEmailAddresses;
    }

    public Set<String> getFilterToEmailAddresses() {
        return filterToEmailAddresses;
    }

    public void setFilterToEmailAddresses(Set<String> filterToEmailAddresses) {
        this.filterToEmailAddresses = filterToEmailAddresses;
    }

    public String getStaticFolderPath() {
        return staticFolderPath;
    }

    public void setStaticFolderPath(String staticFolderPath) {
        this.staticFolderPath = staticFolderPath;
    }

    // Forwar to server settings
    private String forwardServer = "";
    private String forwardServerDomain = "";
    private String forwardServerUserName = "";
    private String forwardServerPassword = "";
    private String forwardEmailAdress = "";

    public String getForwardServer() {
        return this.forwardServer;
    }
    public void setForwardServer(String value) {
        this.forwardServer = value;
    }

    public String getForwardServerDomain() {
        return this.forwardServerDomain;
    }
    public void setforwardServerDomain(String value) {
        this.forwardServerDomain = value;
    }

    public String getForwardServerUserName() {
        return this.forwardServerUserName;
    }
    public void setForwardServerUserName(String value) {
        this.forwardServerUserName = value;
    }

    public String getForwardServerPassword() {
        return this.forwardServerPassword;
    }
    public void setForwardServerPassword(String value) {
        this.forwardServerPassword = value;
    }

    public String getForwardEmailAdress() {
        return this.forwardEmailAdress;
    }
    public void setForwardEmailAdress(String value) {
        this.forwardEmailAdress = value;
    }



}
