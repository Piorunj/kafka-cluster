package com.unis.kafkaproducer.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.unis.kafkaproducer.model.Message;

@Service
public class MessageServiceImpl implements MessageService {

	private static final String TOPIC_MESSAGE = "topic-message";

	private static final String TOPIC_PICTURE = "topic-picture";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private SimpMessagingTemplate socketTemplate;

	@Override
	public void addMessage(Message message) {
		System.out.println("adding message (" + message.getTitle() + " / " + message.getContent() + ")");
		kafkaTemplate.send(TOPIC_MESSAGE, message.getTitle(), message.getContent());
		System.out.println("message added");
	}

	@Override
	public void addPicture(MultipartFile file) {
		String fileAsString;
		try {
			fileAsString = Base64.getEncoder().encodeToString(file.getBytes());
			kafkaTemplate.send(TOPIC_PICTURE, fileAsString);
			System.out.println("picture added");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@KafkaListener(topics = TOPIC_MESSAGE)
	public Message getMessage(String message) {
		System.out.println("Received following message : " + message);
		socketTemplate.convertAndSend("/message", message);
		System.out.println("soccket response");
		return new Message(TOPIC_MESSAGE, message);
	}

	@Override
	@KafkaListener(topics = TOPIC_PICTURE)
	public Message getPicture(String encodedImage) {
		System.out.println("Received new image");
		socketTemplate.convertAndSend("/picture", encodedImage);
		System.out.println("soccket picture response");
		return new Message(TOPIC_MESSAGE, encodedImage);
	}
}
