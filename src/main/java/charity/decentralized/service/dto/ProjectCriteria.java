package charity.decentralized.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import charity.decentralized.domain.enumeration.ProjectType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link charity.decentralized.domain.Project} entity. This class is used
 * in {@link charity.decentralized.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProjectType
     */
    public static class ProjectTypeFilter extends Filter<ProjectType> {

        public ProjectTypeFilter() {
        }

        public ProjectTypeFilter(ProjectTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProjectTypeFilter copy() {
            return new ProjectTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter amount;

    private ProjectTypeFilter projectType;

    private InstantFilter expiredDate;

    private StringFilter description;

    private LongFilter userId;

    public ProjectCriteria(){
    }

    public ProjectCriteria(ProjectCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.projectType = other.projectType == null ? null : other.projectType.copy();
        this.expiredDate = other.expiredDate == null ? null : other.expiredDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public ProjectTypeFilter getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectTypeFilter projectType) {
        this.projectType = projectType;
    }

    public InstantFilter getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(InstantFilter expiredDate) {
        this.expiredDate = expiredDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(projectType, that.projectType) &&
            Objects.equals(expiredDate, that.expiredDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        amount,
        projectType,
        expiredDate,
        description,
        userId
        );
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (projectType != null ? "projectType=" + projectType + ", " : "") +
                (expiredDate != null ? "expiredDate=" + expiredDate + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
