package com.superluli.dsstest.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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

	// normal download, store-and-go
	@RequestMapping(value = "/wallpaper/normal", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getWallPaperNormal() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream("wallpaper.jpg");

		return ResponseEntity.ok().body(IOUtils.toByteArray(in));
	}

	// chunked download, NOTE : transfer encoding is implemented by servlet itself,
	// chunk size is controlled by outputStream.write()
	@RequestMapping(value = "/wallpaper/chunked", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<StreamingResponseBody> getWallPaper() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream("wallpaper.jpg");

		return ResponseEntity.ok().body(new StreamingResponseBody() {

			@Override
			public void writeTo(OutputStream outputStream) throws IOException {

				int size = 1024 * 1024;
				byte[] buffer = new byte[size];
				int bytesRead = -1;
				while ((bytesRead = in.read(buffer)) != -1) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputStream.write(buffer, 0, bytesRead);
				}
			}
		});
	}

	// low level servlet implementation, write to servlet's output stream directly
	@RequestMapping(value = "/wallpaper/lowlevel", method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// response.setHeader("transfer-encoding", "chunked");
		response.setHeader("content-type", MediaType.IMAGE_JPEG_VALUE);
		response.setStatus(200);

		OutputStream outputStream = response.getOutputStream();
		InputStream in = FileService.class.getClassLoader().getResourceAsStream("wallpaper.jpg");

		// 1m chunks
		int size = 1024 * 1024;
		byte[] buffer = new byte[size];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			// outputStream.write((bytesRead + "\r\n").getBytes());
			Thread.sleep(1000);
			outputStream.write(buffer, 0, bytesRead);
		}
	}
}
