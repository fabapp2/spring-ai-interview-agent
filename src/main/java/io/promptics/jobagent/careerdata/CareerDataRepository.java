package io.promptics.jobagent.careerdata;

import com.mongodb.client.result.UpdateResult;
import io.promptics.jobagent.careerdata.model.*;
import io.promptics.jobagent.utils.NanoIdGenerator;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CareerDataRepository {

    private static final Map<Class<?>, String> SECTION_FIELDS = new HashMap<>();

    static {
        SECTION_FIELDS.put(Work.class, "work");
        SECTION_FIELDS.put(Award.class, "awards");
        SECTION_FIELDS.put(Certificate.class, "certificates");
        SECTION_FIELDS.put(Education.class, "education");
        SECTION_FIELDS.put(Interest.class, "interests");
        SECTION_FIELDS.put(Language.class, "languages");
        SECTION_FIELDS.put(Project.class, "projects");
        SECTION_FIELDS.put(Publication.class, "publications");
        SECTION_FIELDS.put(Reference.class, "references");
        SECTION_FIELDS.put(Skill.class, "skills");
        SECTION_FIELDS.put(Volunteer.class, "volunteer");
    }

    private final MongoTemplate mongoTemplate;

    private final NanoIdGenerator idGenerator;
    private final NanoIdGenerator nanoIdGenerator;

    public <T extends SectionWithId> List<T> readSection(String careerDataId, Class<T> section) {
        String fieldName = SECTION_FIELDS.get(section);
        if (fieldName == null) {
            throw new IllegalArgumentException("No field mapping found for class: " + section.getSimpleName());
        }

        Query query = new Query(Criteria.where("_id").is(careerDataId));
        query.fields().include(fieldName);

        Document document = mongoTemplate.findOne(query, Document.class, "career_data");
        if (document == null || document.get(fieldName) == null) {
            throw new UnknownSectionException("Section " + fieldName + " not found for id: " + careerDataId);
        }

        List<Document> subDocs = (List<Document>) document.get(fieldName);
        return subDocs.stream()
                .map(doc -> mongoTemplate.getConverter().read(section, doc))
                .collect(Collectors.toList());
    }

    public void updateBasics(String careerDataId, Basics basics) {
        basics.getProfiles().stream()
                .filter(s -> s.getId() == null)
                .forEach(s -> s.setId(generateId()));
        Query query = new Query(Criteria.where("_id").is(careerDataId));
        Update update = new Update().set("basics", basics);
        mongoTemplate.updateFirst(query, update, CareerData.class);
    }

    public void updateWork(String careerDataId, List<Work> work) {
        updateSection(careerDataId, "work", work);
    }

    public void updateAwards(String careerDataId, List<Award> award) {
        updateSection(careerDataId, "award", award);
    }

    public void updateCertificates(String careerDataId, List<Certificate> certificates) {
        updateSection(careerDataId, "certificates", certificates);
    }

    public void updateEducation(String careerDataId, List<Education> education) {
        updateSection(careerDataId, "education", education);
    }

    public void updateInterests(String careerDataId, List<Interest> interests) {
        updateSection(careerDataId, "interests", interests);
    }

    public void updateLanguages(String careerDataId, List<Language> languages) {
        updateSection(careerDataId, "languages", languages);
    }

    public void updateMeta(String careerDataId, Meta meta) {
        Query query = new Query(Criteria.where("_id").is(careerDataId));
        Update update = new Update().set("meta", meta);
        mongoTemplate.updateFirst(query, update, CareerData.class);
    }

    public void updateProjects(String careerDataId, List<Project> projects) {
        updateSection(careerDataId, "projects", projects);
    }

    public void updatePublications(String careerDataId, List<Publication> publications) {
        updateSection(careerDataId, "publications", publications);
    }

    public void updateReferences(String careerDataId, List<Reference> references) {
        updateSection(careerDataId, "references", references);
    }

    public void updateSkills(String careerDataId, List<Skill> skills) {
        updateSection(careerDataId, "skills", skills);
    }

    public void updateVolunteer(String careerDataId, List<Volunteer> volunteer) {
        updateSection(careerDataId, "volunteer", volunteer);
    }

    private UpdateResult updateSection(String careerDataId, String section,List<? extends SectionWithId> sectionData) {
        Query query = prep(careerDataId, sectionData);
        Update update = new Update().set(section, sectionData);
        return mongoTemplate.updateFirst(query, update, CareerData.class);
    }

    private Query prep(String careerDataId, List<? extends SectionWithId> sectionData) {
        sectionData.stream()
                .filter(s -> s.getId() == null)
                .forEach(s -> s.setId(generateId()));
        Query query = new Query(Criteria.where("_id").is(careerDataId));
        return query;
    }

    public Optional<CareerData> findById(String careerDataId) {
        CareerData careerData = mongoTemplate.findById(careerDataId, CareerData.class);
        return Optional.ofNullable(careerData);
    }

    public CareerData save(CareerData careerData) {
        setMissingIds(careerData.getAwards());
        setMissingIds(careerData.getCertificates());
        setMissingIds(careerData.getEducation());
        setMissingIds(careerData.getInterests());
        setMissingIds(careerData.getLanguages());
        setMissingIds(careerData.getProjects());
        setMissingIds(careerData.getPublications());
        setMissingIds(careerData.getReferences());
        setMissingIds(careerData.getSkills());
        setMissingIds(careerData.getVolunteer());
        setMissingIds(careerData.getWork());
        setMissingIds(careerData.getBasics().getProfiles());
        return mongoTemplate.save(careerData);
    }

    public void addWork(String careerDataId, Work work) {
        Query query = prep(careerDataId, List.of(work));
        Update update = new Update().push("work", work);
        mongoTemplate.updateFirst(query, update, CareerData.class);
    }

    private void setMissingIds(List<? extends SectionWithId> section) {
        if (section != null) {
            section.stream()
                    .filter(w -> w.getId() == null)
                    .forEach(w -> w.setId(generateId()));
        }
    }

    public String generateId() {
        return nanoIdGenerator.generateId();
    }
}
