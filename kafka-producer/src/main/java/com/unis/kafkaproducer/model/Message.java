package com.unis.kafkaproducer.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6947995793358598219L;
	private String title;
	private String content;
}
