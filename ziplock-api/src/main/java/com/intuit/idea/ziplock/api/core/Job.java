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
@javax.persistence.Table(name = "job")
@NamedQueries({
        @NamedQuery(
                name = "com.intuit.idea.ziplock.api.core.Job.findAll",
                query = "SELECT j FROM Job j"
        ),
        @NamedQuery(
                name = "job.findById2",
                query = "select distinct jobs from Person as p left join p.job as jobs where p.id = :ID"
//                query = "select distinct p from Person as p left join fetch p.job where p.id = :ID"
        )
})
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @javax.persistence.Column(name = "paymentType", nullable = false)
    private String paymentType;

    @javax.persistence.Column(name = "perks", nullable = false)
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