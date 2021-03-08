package com.unis.kafkaproducer.service;

import org.springframework.web.multipart.MultipartFile;

import com.unis.kafkaproducer.model.Message;

public interface MessageService {

	public void addMessage(Message message);

	public Message getMessage(String topic);

	void addPicture(MultipartFile file);

	Message getPicture(String message);

	public void spamMessage();
}
