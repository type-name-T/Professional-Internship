package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Document entity.
 * 
 * @author bgamard
 */
@Entity
@Table(name = "T_DOCUMENT")
public class Document implements Loggable {
    /**
     * Document ID.
     */
    @Id
    @Column(name = "DOC_ID_C", length = 36)
    private String id;
    
    /**
     * User ID.
     */
    @Column(name = "DOC_IDUSER_C", nullable = false, length = 36)
    private String userId;
    
    /**
     * Main file ID.
     */
    @Column(name = "DOC_IDFILE_C", length = 36)
    private String fileId;

    /**
     * Language (ISO 639-9).
     */
    @Column(name = "DOC_LANGUAGE_C", nullable = false, length = 3)
    private String language;
    
    /**
     * Title.
     */
    @Column(name = "DOC_TITLE_C", nullable = false, length = 100)
    private String title;
    
    /**
     * Description.
     */
    @Column(name = "DOC_DESCRIPTION_C", length = 4000)
    private String description;
    
    /**
     * Subject.
     */
    @Column(name = "DOC_SUBJECT_C", length = 500)
    private String subject;
    
    /**
     * Identifer.
     */
    @Column(name = "DOC_IDENTIFIER_C", length = 500)
    private String identifier;
    
    /**
     * Publisher.
     */
    @Column(name = "DOC_PUBLISHER_C", length = 500)
    private String publisher;
    
    /**
     * Format.
     */
    @Column(name = "DOC_FORMAT_C", length = 500)
    private String format;
    
    /**
     * Source.
     */
    @Column(name = "DOC_SOURCE_C", length = 500)
    private String source;
    
    /**
     * Type.
     */
    @Column(name = "DOC_TYPE_C", length = 100)
    private String type;
    
    /**
     * Coverage.
     */
    @Column(name = "DOC_COVERAGE_C", length = 100)
    private String coverage;
    
    /**
     * Rights.
     */
    @Column(name = "DOC_RIGHTS_C", length = 100)
    private String rights;

    /**
     * Document classification ID (收文/发文/会议纪要等).
     */
    @Column(name = "DOC_IDCLASSIFICATION_C", length = 36)
    private String classificationId;

    /**
     * Secrecy level (公开/内部/秘密/机密/绝密).
     */
    @Column(name = "DOC_SECRECYLEVEL_C", length = 20)
    private String secrecyLevel;

    /**
     * Urgency level (一般/紧急/特急).
     */
    @Column(name = "DOC_URGENCY_C", length = 20)
    private String urgency;

    /**
     * Document number (发文字号/收文号).
     */
    @Column(name = "DOC_DOCNO_C", length = 100)
    private String docNo;

    /**
     * From unit (来文单位，收文用).
     */
    @Column(name = "DOC_FROMUNIT_C", length = 200)
    private String fromUnit;

    /**
     * Handler department ID (承办部门).
     */
    @Column(name = "DOC_IDHANDLERDEPT_C", length = 36)
    private String handlerDeptId;

    /**
     * Handler user ID (承办人).
     */
    @Column(name = "DOC_IDHANDLERUSER_C", length = 36)
    private String handlerUserId;

    /**
     * Document date (成文日期).
     */
    @Column(name = "DOC_DOCDATE_D")
    private Date docDate;

    /**
     * Retention period (保管期限：永久/长期/短期).
     */
    @Column(name = "DOC_RETENTION_C", length = 20)
    private String retention;

    /**
     * Archive number (归档号).
     */
    @Column(name = "DOC_ARCHIVENO_C", length = 100)
    private String archiveNo;

    /**
     * Document status (拟稿/审核中/已批准/已签发/已归档/已驳回).
     */
    @Column(name = "DOC_STATUS_C", length = 20)
    private String status;

    /**
     * Creation date.
     */
    @Column(name = "DOC_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Creation date.
     */
    @Column(name = "DOC_UPDATEDATE_D", nullable = false)
    private Date updateDate;

    /**
     * Deletion date.
     */
    @Column(name = "DOC_DELETEDATE_D")
    private Date deleteDate;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileId() {
        return fileId;
    }

    public Document setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public Document setClassificationId(String classificationId) {
        this.classificationId = classificationId;
        return this;
    }

    public String getSecrecyLevel() {
        return secrecyLevel;
    }

    public Document setSecrecyLevel(String secrecyLevel) {
        this.secrecyLevel = secrecyLevel;
        return this;
    }

    public String getUrgency() {
        return urgency;
    }

    public Document setUrgency(String urgency) {
        this.urgency = urgency;
        return this;
    }

    public String getDocNo() {
        return docNo;
    }

    public Document setDocNo(String docNo) {
        this.docNo = docNo;
        return this;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public Document setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
        return this;
    }

    public String getHandlerDeptId() {
        return handlerDeptId;
    }

    public Document setHandlerDeptId(String handlerDeptId) {
        this.handlerDeptId = handlerDeptId;
        return this;
    }

    public String getHandlerUserId() {
        return handlerUserId;
    }

    public Document setHandlerUserId(String handlerUserId) {
        this.handlerUserId = handlerUserId;
        return this;
    }

    public Date getDocDate() {
        return docDate;
    }

    public Document setDocDate(Date docDate) {
        this.docDate = docDate;
        return this;
    }

    public String getRetention() {
        return retention;
    }

    public Document setRetention(String retention) {
        this.retention = retention;
        return this;
    }

    public String getArchiveNo() {
        return archiveNo;
    }

    public Document setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Document setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }

    @Override
    public String toMessage() {
        return title;
    }
}
