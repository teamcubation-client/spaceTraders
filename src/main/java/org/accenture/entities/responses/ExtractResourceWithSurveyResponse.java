package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Cargo;
import org.accenture.entities.Cooldown;
import org.accenture.entities.Extraction;

@Getter
public class ExtractResourceWithSurveyResponse {
    private Cooldown cooldown;
    private Extraction extraction;
    private Cargo cargo;
}
