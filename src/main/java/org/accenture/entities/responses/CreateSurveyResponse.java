package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Cooldown;
import org.accenture.entities.Survey;

@Getter
public class CreateSurveyResponse {
    private Cooldown cooldown;
    private Survey[] surveys;
}
