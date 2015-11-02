package com.intuit.idea.ziplock.api.core;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "job")
@NamedQueries({
        @NamedQuery(
                name = "com.intuit.idea.ziplock.api.core.Job.findAll",
                query = "SELECT j FROM Job j"
        )
})
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "paymentType", nullable = false)
    private String paymentType;

    @Column(name = "perks", nullable = false)
    private String perks;

    public Job() {
    }

    public Job(String paymentType, String perks) {
        this.paymentType = paymentType;
        this.perks = perks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPerks() {
        return perks;
    }

    public void setPerks(String perks) {
        this.perks = perks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }

        final Job that = (Job) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.paymentType, that.paymentType) &&
                Objects.equals(this.perks, that.perks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentType, perks);
    }
}