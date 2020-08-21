package org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialPK implements Serializable {

    private long userId;
    private String credentialType;

    @Column(name = "user_id", nullable = false)
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    @Column(name = "credential_type", nullable = false)
    @Id
    public String getCredentialType() {
        return credentialType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialPK that = (CredentialPK) o;
        return userId == that.userId &&
                Objects.equals(credentialType, that.credentialType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, credentialType);
    }

}
