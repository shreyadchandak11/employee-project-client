package com.hdfc.capstone.employee.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

	@Configuration
	public class Config {

		@Bean
		public RestTemplate getResttemplate() {
		KeyStore trustStore=null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileInputStream instream = null;
		try {
			instream = new FileInputStream(new File("src\\main\\resources\\capstoneemployee.jks"));
		    trustStore.load(instream, "shreeom".toCharArray());

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		RestTemplate restTemplate = new RestTemplate();
		SSLContext sslContext = null;
		try {
			sslContext = SSLContextBuilder.create()
			    .loadTrustMaterial(trustStore, null)
			    .build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
		    sslContext,
		    NoopHostnameVerifier.INSTANCE);
		HttpClient httpClient = HttpClients.custom()
		    .setSSLSocketFactory(socketFactory)
		    .build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(factory);
		 return restTemplate;
	     
		}
		
	}   