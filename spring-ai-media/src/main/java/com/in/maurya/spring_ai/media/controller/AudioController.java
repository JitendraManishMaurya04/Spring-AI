package com.in.maurya.spring_ai.media.controller;

import com.in.maurya.spring_ai.media.service.AudioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ai/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService){
        this.audioService = audioService;
    }

    @PostMapping("/transcript/audio-to-text")
    public ResponseEntity<String> transcriptAudio(@Value("classpath:/audio/sample2.m4a") Resource audioInput){
        String  response = audioService.convertAudioToText(audioInput);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transcript-option/audio-to-text")
    public ResponseEntity<String> transcriptAudioWithOptions(@Value("classpath:/audio/sample2.m4a") Resource audioInput){
        String  response = audioService.convertAudioToTextWithOptions(audioInput);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transcript/audio-to-text/fileInput")
    public ResponseEntity<String> transcriptAudio(@RequestParam("audioFile") MultipartFile audioFile){
        String  response = audioService.convertAudioToText(audioFile.getResource());
        return ResponseEntity.ok(response);
    }
}
