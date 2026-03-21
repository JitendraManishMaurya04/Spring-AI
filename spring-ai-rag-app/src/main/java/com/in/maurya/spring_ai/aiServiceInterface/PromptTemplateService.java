package com.in.maurya.spring_ai.aiServiceInterface;

import com.in.maurya.spring_ai.dto.GenericResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

public interface PromptTemplateService {

    Prompt promptTemplateBuilder(String place, String days, String budget, boolean isTrekReq );
}
