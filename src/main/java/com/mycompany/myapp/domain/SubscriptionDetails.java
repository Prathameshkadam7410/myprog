package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SubscriptionDetails.
 */
@Table("subscription_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("subscription_name")
    private String subscriptionName;

    @NotNull(message = "must not be null")
    @Column("subscription_amount")
    private Integer subscriptionAmount;

    @NotNull(message = "must not be null")
    @Column("tax_amount")
    private Integer taxAmount;

    @NotNull(message = "must not be null")
    @Column("total_amount")
    private Integer totalAmount;

    @NotNull(message = "must not be null")
    @Column("subscription_start_date")
    private Instant subscriptionStartDate;

    @NotNull(message = "must not be null")
    @Column("subscription_expiry_date")
    private Instant subscriptionExpiryDate;

    @NotNull(message = "must not be null")
    @Column("additional_comments")
    private String additionalComments;

    @NotNull(message = "must not be null")
    @Column("category")
    private String category;

    @NotNull(message = "must not be null")
    @Column("notification_before_expiry")
    private Integer notificationBeforeExpiry;

    @NotNull(message = "must not be null")
    @Column("notification_mute_flag")
    private Boolean notificationMuteFlag;

    @NotNull(message = "must not be null")
    @Column("notification_to")
    private String notificationTo;

    @Column("notification_cc")
    private String notificationCc;

    @Column("notification_bcc")
    private String notificationBcc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public SubscriptionDetails subscriptionName(String subscriptionName) {
        this.setSubscriptionName(subscriptionName);
        return this;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Integer getSubscriptionAmount() {
        return this.subscriptionAmount;
    }

    public SubscriptionDetails subscriptionAmount(Integer subscriptionAmount) {
        this.setSubscriptionAmount(subscriptionAmount);
        return this;
    }

    public void setSubscriptionAmount(Integer subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Integer getTaxAmount() {
        return this.taxAmount;
    }

    public SubscriptionDetails taxAmount(Integer taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(Integer taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }

    public SubscriptionDetails totalAmount(Integer totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getSubscriptionStartDate() {
        return this.subscriptionStartDate;
    }

    public SubscriptionDetails subscriptionStartDate(Instant subscriptionStartDate) {
        this.setSubscriptionStartDate(subscriptionStartDate);
        return this;
    }

    public void setSubscriptionStartDate(Instant subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public Instant getSubscriptionExpiryDate() {
        return this.subscriptionExpiryDate;
    }

    public SubscriptionDetails subscriptionExpiryDate(Instant subscriptionExpiryDate) {
        this.setSubscriptionExpiryDate(subscriptionExpiryDate);
        return this;
    }

    public void setSubscriptionExpiryDate(Instant subscriptionExpiryDate) {
        this.subscriptionExpiryDate = subscriptionExpiryDate;
    }

    public String getAdditionalComments() {
        return this.additionalComments;
    }

    public SubscriptionDetails additionalComments(String additionalComments) {
        this.setAdditionalComments(additionalComments);
        return this;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getCategory() {
        return this.category;
    }

    public SubscriptionDetails category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNotificationBeforeExpiry() {
        return this.notificationBeforeExpiry;
    }

    public SubscriptionDetails notificationBeforeExpiry(Integer notificationBeforeExpiry) {
        this.setNotificationBeforeExpiry(notificationBeforeExpiry);
        return this;
    }

    public void setNotificationBeforeExpiry(Integer notificationBeforeExpiry) {
        this.notificationBeforeExpiry = notificationBeforeExpiry;
    }

    public Boolean getNotificationMuteFlag() {
        return this.notificationMuteFlag;
    }

    public SubscriptionDetails notificationMuteFlag(Boolean notificationMuteFlag) {
        this.setNotificationMuteFlag(notificationMuteFlag);
        return this;
    }

    public void setNotificationMuteFlag(Boolean notificationMuteFlag) {
        this.notificationMuteFlag = notificationMuteFlag;
    }

    public String getNotificationTo() {
        return this.notificationTo;
    }

    public SubscriptionDetails notificationTo(String notificationTo) {
        this.setNotificationTo(notificationTo);
        return this;
    }

    public void setNotificationTo(String notificationTo) {
        this.notificationTo = notificationTo;
    }

    public String getNotificationCc() {
        return this.notificationCc;
    }

    public SubscriptionDetails notificationCc(String notificationCc) {
        this.setNotificationCc(notificationCc);
        return this;
    }

    public void setNotificationCc(String notificationCc) {
        this.notificationCc = notificationCc;
    }

    public String getNotificationBcc() {
        return this.notificationBcc;
    }

    public SubscriptionDetails notificationBcc(String notificationBcc) {
        this.setNotificationBcc(notificationBcc);
        return this;
    }

    public void setNotificationBcc(String notificationBcc) {
        this.notificationBcc = notificationBcc;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDetails{" +
            "id=" + getId() +
            ", subscriptionName='" + getSubscriptionName() + "'" +
            ", subscriptionAmount=" + getSubscriptionAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", subscriptionStartDate='" + getSubscriptionStartDate() + "'" +
            ", subscriptionExpiryDate='" + getSubscriptionExpiryDate() + "'" +
            ", additionalComments='" + getAdditionalComments() + "'" +
            ", category='" + getCategory() + "'" +
            ", notificationBeforeExpiry=" + getNotificationBeforeExpiry() +
            ", notificationMuteFlag='" + getNotificationMuteFlag() + "'" +
            ", notificationTo='" + getNotificationTo() + "'" +
            ", notificationCc='" + getNotificationCc() + "'" +
            ", notificationBcc='" + getNotificationBcc() + "'" +
            "}";
    }
}
