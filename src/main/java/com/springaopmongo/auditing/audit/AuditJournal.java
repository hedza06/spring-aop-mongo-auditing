package com.springaopmongo.auditing.audit;

import com.springaopmongo.auditing.utils.JsonToMapConverter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "journal")
public class AuditJournal {

    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String auditor;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @NotNull @NotEmpty
    private String route;

    @NotNull @NotEmpty
    private String method;

    @NotNull @NotEmpty
    private String signatureName;

    @NotNull @NotEmpty
    private String signatureDeclaringType;

    @NotNull @NotEmpty
    private String actionType;

    @Convert(attributeName = "data", converter = JsonToMapConverter.class)
    private Map<String, Object> requestData = new HashMap<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getSignatureDeclaringType() {
        return signatureDeclaringType;
    }

    public void setSignatureDeclaringType(String signatureDeclaringType) {
        this.signatureDeclaringType = signatureDeclaringType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Map<String, Object> getRequestData() {
        return requestData;
    }

    public void setRequestData(Map<String, Object> requestData) {
        this.requestData = requestData;
    }

    @Override
    public String toString()
    {
        return "AuditJournal{" +
                "id='" + id + '\'' +
                ", auditor='" + auditor + '\'' +
                ", timestamp=" + timestamp +
                ", route='" + route + '\'' +
                ", method='" + method + '\'' +
                ", signatureName='" + signatureName + '\'' +
                ", signatureDeclaringType'" + signatureDeclaringType +'\'' +
                ", actionType'" + actionType +'\'' +
                ", requestData=" + requestData +
                '}';
    }
}
