package org.springai.imagedemo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping("/image")
public class DashScopeImageController {

	private final ImageModel imageModel;

	private static final String DEFAULT_PROMPT = "为人工智能生成一张富有科技感的图片！";

	public DashScopeImageController(ImageModel imageModel) {
		this.imageModel = imageModel;
	}

	@GetMapping("/demo")
	public void image(HttpServletResponse response) {

		ImageResponse imageResponse = imageModel.call(new ImagePrompt(DEFAULT_PROMPT));
		String imageUrl = imageResponse.getResult().getOutput().getUrl();

		try {
			URL url = URI.create(imageUrl).toURL();
			InputStream in = url.openStream();

			response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
			response.getOutputStream().write(in.readAllBytes());
			response.getOutputStream().flush();
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
