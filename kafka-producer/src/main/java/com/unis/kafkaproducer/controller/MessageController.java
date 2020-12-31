package com.unis.kafkaproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unis.kafkaproducer.model.Message;
import com.unis.kafkaproducer.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping("/{content}")
	public void postMessage(@PathVariable String content) {
		messageService.addMessage(new Message("title", content));
	}

	@PostMapping("/picture")
	public String postPicture(@RequestParam("files") MultipartFile files, ModelMap modelMap) {
		System.out.println("file recieved");
		messageService.addPicture(files);
		return "fileUploadView";
	}

}
