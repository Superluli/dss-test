package com.superluli.dsstest.app;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

//@Service
public class FileUploadService {

	public static final String BUCKET_NAME = "superluli.test-bucket";

	@Value("${aws.accessKeyId}")
	private String accessKey;

	@Value("${aws.secretKey}")
	private String secretKey;

	public void uploadFile() {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new SystemPropertiesCredentialsProvider())
				.build();
		ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(BUCKET_NAME));
		System.err.println(s3Client.getS3AccountOwner().getDisplayName());
		s3Client.putObject(BUCKET_NAME, "test-folder/stringfile", "LOL");
		System.err.println("DONE!");
	}

	@PostConstruct
	public void init() {
		System.setProperty("aws.accessKeyId", accessKey);
		System.setProperty("aws.secretKey", secretKey);
		uploadFile();
	}
}
