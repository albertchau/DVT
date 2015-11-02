package com.intuit.idea.ziplock.api.core;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "people")
@NamedQueries({
        @NamedQuery(
                name = "person.findAll",
                query = "select distinct p from Person as p left join fetch p.job"
        ),
        @NamedQuery(
                name = "person.findById",
                query = "select distinct p from Person as p left join fetch p.job where p.id = ?"
        )
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "jobTitle", nullable = false)
    private String jobTitle;

    @OneToMany
    private List<Job> job = new ArrayList<>();

    public Person() {
    }

    public Person(String fullName, String jobTitle) {
        this.fullName = fullName;
        this.jobTitle = jobTitle;
    }

    public List<Job> getJob() {
        return job;
    }

    public void setJob(List<Job> job) {
        this.job = job;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }

        final Person that = (Person) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.fullName, that.fullName) &&
                Objects.equals(this.jobTitle, that.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, jobTitle, job);
    }
}