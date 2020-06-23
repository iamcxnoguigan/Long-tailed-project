package l2l.contextsharing.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty(value = "Subject")
    private String subject;

    @JsonProperty(value = "Type")
    private String type;

    @JsonProperty(value = "MessageId")
    private String messageId;

    @JsonProperty(value = "Token")
    private String token;

    @JsonProperty(value = "TopicArn")
    private String topicArn;

    @JsonProperty(value = "Message")
    private String message;

    @JsonProperty(value = "SubscribeURL")
    private String subscribeURL;

    @JsonProperty(value = "Timestamp")
    private String timestamp;

    @JsonProperty(value = "SignatureVersion")
    private String signatureVersion;

    @JsonProperty(value = "Signature")
    private String signature;

    @JsonProperty(value = "SigningCertURL")
    private String signingCertURL;

    @JsonProperty(value = "UnsubscribeURL")
    private String unsubscribeURL;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubscribeURL() {
        return subscribeURL;
    }

    public void setSubscribeURL(String subscribeURL) {
        this.subscribeURL = subscribeURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSigningCertURL() {
        return signingCertURL;
    }

    public void setSigningCertURL(String signingCertURL) {
        this.signingCertURL = signingCertURL;
    }

    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }

    @Override
    public String toString() {
        return "Message [subject=" + subject + ", type=" + type + ", messageId=" + messageId + ", token=" + token
                + ", topicArn=" + topicArn + ", message=" + message + ", subscribeURL=" + subscribeURL + ", timestamp="
                + timestamp + ", signatureVersion=" + signatureVersion + ", signature=" + signature
                + ", signingCertURL=" + signingCertURL + ", unsubscribeURL=" + unsubscribeURL + "]";
    }
}
