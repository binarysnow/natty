package com.binarysnow.natty.frame.server;

import com.binarysnow.natty.io.Command;
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
    private Boolean tldVerify;

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
}
