
package io.promptics.jobagent.careerdata.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Resume Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "$schema",
    "basics",
    "work",
    "volunteer",
    "education",
    "awards",
    "certificates",
    "publications",
    "skills",
    "languages",
    "interests",
    "references",
    "projects",
    "meta"
})
@Document(collection = "career_data")
public class CareerData {

    @Id
    private String id;

    /**
     * link to the version of the schema that can validate the resume
     */
    @JsonProperty("$schema")
    @JsonPropertyDescription("link to the version of the schema that can validate the resume")
    private URI $schema;
    @JsonProperty("basics")
    @Valid
    private Basics basics;
    @JsonProperty("work")
    @Valid
    private List<Work> work;
    @JsonProperty("volunteer")
    @Valid
    private List<Volunteer> volunteer;
    @JsonProperty("education")
    @Valid
    private List<Education> education;
    /**
     * Specify any awards you have received throughout your professional career
     * 
     */
    @JsonProperty("awards")
    @JsonPropertyDescription("Specify any awards you have received throughout your professional career")
    @Valid
    private List<Award> awards;
    /**
     * Specify any certificates you have received throughout your professional career
     * 
     */
    @JsonProperty("certificates")
    @JsonPropertyDescription("Specify any certificates you have received throughout your professional career")
    @Valid
    private List<Certificate> certificates;
    /**
     * Specify your publications through your career
     * 
     */
    @JsonProperty("publications")
    @JsonPropertyDescription("Specify your publications through your career")
    @Valid
    private List<Publication> publications;
    /**
     * List out your professional skill-set
     * 
     */
    @JsonProperty("skills")
    @JsonPropertyDescription("List out your professional skill-set")
    @Valid
    private List<Skill> skills;
    /**
     * List any other languages you speak
     * 
     */
    @JsonProperty("languages")
    @JsonPropertyDescription("List any other languages you speak")
    @Valid
    private List<Language> languages;
    @JsonProperty("interests")
    @Valid
    private List<Interest> interests;
    /**
     * List references you have received
     * 
     */
    @JsonProperty("references")
    @JsonPropertyDescription("List references you have received")
    @Valid
    private List<Reference> references;
    /**
     * Specify career projects
     * 
     */
    @JsonProperty("projects")
    @JsonPropertyDescription("Specify career projects")
    @Valid
    private List<Project> projects;
    /**
     * The schema version and any other tooling configuration lives here
     * 
     */
    @JsonProperty("meta")
    @JsonPropertyDescription("The schema version and any other tooling configuration lives here")
    @Valid
    private Meta meta;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * link to the version of the schema that can validate the resume
     * 
     */
    @JsonProperty("$schema")
    public URI get$schema() {
        return $schema;
    }

    /**
     * link to the version of the schema that can validate the resume
     * 
     */
    @JsonProperty("$schema")
    public void set$schema(URI $schema) {
        this.$schema = $schema;
    }

    @JsonProperty("basics")
    public Basics getBasics() {
        return basics;
    }

    @JsonProperty("basics")
    public void setBasics(Basics basics) {
        this.basics = basics;
    }

    @JsonProperty("work")
    public List<Work> getWork() {
        return work;
    }

    @JsonProperty("work")
    public void setWork(List<Work> work) {
        this.work = work;
    }

    @JsonProperty("volunteer")
    public List<Volunteer> getVolunteer() {
        return volunteer;
    }

    @JsonProperty("volunteer")
    public void setVolunteer(List<Volunteer> volunteer) {
        this.volunteer = volunteer;
    }

    @JsonProperty("education")
    public List<Education> getEducation() {
        return education;
    }

    @JsonProperty("education")
    public void setEducation(List<Education> education) {
        this.education = education;
    }

    /**
     * Specify any awards you have received throughout your professional career
     * 
     */
    @JsonProperty("awards")
    public List<Award> getAwards() {
        return awards;
    }

    /**
     * Specify any awards you have received throughout your professional career
     * 
     */
    @JsonProperty("awards")
    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    /**
     * Specify any certificates you have received throughout your professional career
     * 
     */
    @JsonProperty("certificates")
    public List<Certificate> getCertificates() {
        return certificates;
    }

    /**
     * Specify any certificates you have received throughout your professional career
     * 
     */
    @JsonProperty("certificates")
    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    /**
     * Specify your publications through your career
     * 
     */
    @JsonProperty("publications")
    public List<Publication> getPublications() {
        return publications;
    }

    /**
     * Specify your publications through your career
     * 
     */
    @JsonProperty("publications")
    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    /**
     * List out your professional skill-set
     * 
     */
    @JsonProperty("skills")
    public List<Skill> getSkills() {
        return skills;
    }

    /**
     * List out your professional skill-set
     * 
     */
    @JsonProperty("skills")
    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    /**
     * List any other languages you speak
     * 
     */
    @JsonProperty("languages")
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * List any other languages you speak
     * 
     */
    @JsonProperty("languages")
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    @JsonProperty("interests")
    public List<Interest> getInterests() {
        return interests;
    }

    @JsonProperty("interests")
    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    /**
     * List references you have received
     * 
     */
    @JsonProperty("references")
    public List<Reference> getReferences() {
        return references;
    }

    /**
     * List references you have received
     * 
     */
    @JsonProperty("references")
    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    /**
     * Specify career projects
     * 
     */
    @JsonProperty("projects")
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Specify career projects
     * 
     */
    @JsonProperty("projects")
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * The schema version and any other tooling configuration lives here
     * 
     */
    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    /**
     * The schema version and any other tooling configuration lives here
     * 
     */
    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
