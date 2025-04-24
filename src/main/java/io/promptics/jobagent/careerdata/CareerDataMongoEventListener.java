package io.promptics.jobagent.careerdata;

import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.SectionWithId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CareerDataMongoEventListener extends AbstractMongoEventListener<CareerData> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<CareerData> event) {
        CareerData source = event.getSource();

        setMissingIds(source.getAwards());
        setMissingIds(source.getCertificates());
        setMissingIds(source.getEducation());
        setMissingIds(source.getInterests());
        setMissingIds(source.getLanguages());
        setMissingIds(source.getProjects());
        setMissingIds(source.getPublications());
        setMissingIds(source.getReferences());
        setMissingIds(source.getSkills());
        setMissingIds(source.getVolunteer());
        setMissingIds(source.getWork());
        setMissingIds(source.getBasics().getProfiles());
    }

    private void setMissingIds(List<? extends SectionWithId> section) {
        if (section != null) {
            section.stream()
                    .filter(w -> w.getId() == null)
                    .forEach(w -> w.setId(getId()));
        }
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
