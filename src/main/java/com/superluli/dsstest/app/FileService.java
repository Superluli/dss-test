package com.superluli.dsstest.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/files")
public class FileService {

	// normal json
	@RequestMapping(value = "/json", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadJson(HttpServletRequest request, @RequestParam MultiValueMap<String, String> params,
			@RequestHeader HttpHeaders requestHeaders, HttpEntity<String> requestEntity) throws Exception {

		System.err.println("request params : " + params);
		System.err.println("headers : " + requestHeaders);

		System.err.println("headders : " + requestEntity.getHeaders());
		System.err.println(requestEntity.getBody());

		return "GOOD";
	}

	// upload files using multipart
	@RequestMapping(value = "/multipart", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadMP(HttpServletRequest request, @RequestParam MultiValueMap<String, String> params,
			@RequestHeader HttpHeaders requestHeaders, @RequestPart("name") String name,
			@RequestPart("file") MultipartFile file) throws Exception {

		System.err.println("request params : " + params);
		System.err.println("headers : " + requestHeaders);
		System.err.println(file.getSize());
 
		System.err.println("name : " + name);
		int next = -1;
		InputStream in = file.getInputStream();
		byte[] buffer = new byte[1024];
		while ((next = in.read(buffer)) != -1) {
			System.err.println(Arrays.toString(buffer));
		}
		in.close();
		return "GOOD";
	}

	// octet-stream
	@RequestMapping(value = "/octet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadOctet(HttpServletRequest request, @RequestParam MultiValueMap<String, String> params,
			@RequestHeader HttpHeaders requestHeaders, InputStream in) throws Exception {

		System.err.println("request params : " + params);
		System.err.println("headers : " + requestHeaders);
		int next = -1;
		byte[] buffer = new byte[128];
		while ((next = in.read(buffer)) != -1) {
			System.err.println(Arrays.toString(buffer));
		}
		in.close();
		return "GOOD";
	}
}
