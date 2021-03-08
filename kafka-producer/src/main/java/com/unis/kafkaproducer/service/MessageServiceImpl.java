package com.unis.kafkaproducer.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Random;

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

	private static final int NUMBER_MESSAGE_TO_SPAM = 100;

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

	@Override
	public void spamMessage() {
		for (int i = 0; i < NUMBER_MESSAGE_TO_SPAM; i++) {
			kafkaTemplate.send(TOPIC_MESSAGE, "", generateRandomString());
		}
	}

	private String generateRandomString() {
		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		Random rand = new Random();

		// Obtain a number between [0 - 49].
		int n = rand.nextInt(50);
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}
}
