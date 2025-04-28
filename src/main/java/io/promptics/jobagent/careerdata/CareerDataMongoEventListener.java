package io.promptics.jobagent.careerdata;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.SectionWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CareerDataMongoEventListener extends AbstractMongoEventListener<CareerData> {

    private final SectionIdProvider idProvider;

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
                    .forEach(w -> w.setId(idProvider.getId()));
        }
    }

}
