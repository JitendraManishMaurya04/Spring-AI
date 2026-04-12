package com.in.maurya.spring_ai.media.service;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AudioService {

    private final TranscriptionModel transcriptionModel;

    public AudioService(TranscriptionModel transcriptionModel){
        this.transcriptionModel = transcriptionModel;
    }

    public String convertAudioToText(Resource audioInput) {
        return transcriptionModel.transcribe(audioInput);
    }

    public String convertAudioToTextWithOptions(Resource audioInput) {
        return transcriptionModel.transcribe(audioInput,
                OpenAiAudioTranscriptionOptions.builder()
                        .language("en")
                        .temperature(0.7f)
                        .build());
    }
}
