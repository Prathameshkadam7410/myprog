package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmailConfig.
 */
@Table("email_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("email_id")
    private String emailId;

    @Column("token_string")
    private String tokenString;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public EmailConfig emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTokenString() {
        return this.tokenString;
    }

    public EmailConfig tokenString(String tokenString) {
        this.setTokenString(tokenString);
        return this;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailConfig{" +
            "id=" + getId() +
            ", emailId='" + getEmailId() + "'" +
            ", tokenString='" + getTokenString() + "'" +
            "}";
    }
}
