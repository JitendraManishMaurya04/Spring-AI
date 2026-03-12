package com.in.maurya.spring_ai.aiServiceInterface;

import com.in.maurya.spring_ai.dto.GenericResponse;

import java.util.List;

public interface PromptService {

    List<GenericResponse> chat(String prompt);

    String chat(String prompt,String persona);

    String chatMetaData(String prompt, String persona);

    String travelPlannerChat(String place, String days, String budget);

    String travelPlannerChat(String place, String days, String budget, boolean isTrekReq);

}
