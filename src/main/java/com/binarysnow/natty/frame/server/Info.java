package com.binarysnow.natty.frame.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class Info implements Command {
    @SerializedName("server_id")
    private String serverId;

    @SerializedName("version")
    private String serverVersion;

    @SerializedName("go")
    private String goVersion;

    @SerializedName("host")
    private String host;

    @SerializedName("port")
    private Integer port;

    @SerializedName("auth_required")
    private Boolean authenticationRequired;

    @SerializedName("ssl_required")
    private Boolean sslRequired;

    @SerializedName("tls_required")
    private Boolean tlsRequired;

    @SerializedName("tls_verify")
    private Boolean tlsVerify;

    @SerializedName("max_payload")
    private Long maximumPayload;

    @SerializedName("connect_urls")
    private List<String> connectUrls;

    private static final transient Gson gson = new GsonBuilder().create();

    public static Info parseString(final String jsonString) {
        return gson.fromJson(jsonString, Info.class);
    }

    private Info() {
        // Hide the constructor
    }

    public String toString() {
        return gson.toJson(this);
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode.INFO;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(final String serverId) {
        this.serverId = serverId;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(final String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getGoVersion() {
        return goVersion;
    }

    public void setGoVersion(final String goVersion) {
        this.goVersion = goVersion;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(final Boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    public Boolean getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(final Boolean sslRequired) {
        this.sslRequired = sslRequired;
    }

    public Boolean getTlsRequired() {
        return tlsRequired;
    }

    public void setTlsRequired(final Boolean tlsRequired) {
        this.tlsRequired = tlsRequired;
    }

    public Boolean getTlsVerify() {
        return tlsVerify;
    }

    public void setTlsVerify(final Boolean tlsVerify) {
        this.tlsVerify = tlsVerify;
    }

    public Long getMaximumPayload() {
        return maximumPayload;
    }

    /**
     * Set the maximum payload size
     * @param maximumPayload The maximum payload size in bytes
     */
    public void setMaximumPayload(final Long maximumPayload) {
        this.maximumPayload = maximumPayload;
    }

    public List<String> getConnectUrls() {
        return connectUrls;
    }

    public void setConnectUrls(final List<String> connectUrls) {
        this.connectUrls = connectUrls;
    }
}
