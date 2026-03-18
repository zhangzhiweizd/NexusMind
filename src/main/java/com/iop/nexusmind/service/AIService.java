package com.iop.nexusmind.service;

public interface AIService {
    
    String generateSummary(String content);
    
    String chat(String message);
}
