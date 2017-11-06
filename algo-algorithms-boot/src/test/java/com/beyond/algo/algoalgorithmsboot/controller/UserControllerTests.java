package com.beyond.algo.algoalgorithmsboot.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

	@Autowired
	protected WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}
	@Test
	public void contextLoads() {

	}
	@Test
	public void registerUser() throws Exception{


		String result = this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
				.param("usrCode","zhang1")
				.param("passwd","12345678")
				.param("usrName","智")
				.param("email","test1@qq.com"))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}

	@Test
	public void testOAuth2() throws Exception{

		String result = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
				.param("usrName","qihe")
				.param("passwd","12345678"))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}

	@Test
	public void updateUserInformation() throws Exception{

		String result = this.mockMvc.perform(post("/updateUserInformation").contentType(MediaType.APPLICATION_JSON)
				.param("usrSn","c0a19c1576174b3f97924e75f05f9596")
				.param("usrName","张传智智")
				.param("email","zhangchuanzhiTest@126.com")
				.param("needNotify","0")
				.param("telephone","13252912247")
				.param("usrUrl","https://www.11145.com"))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}
	@Test
	public void accountInformation() throws Exception{

		String result = this.mockMvc.perform(get("/accountInformation").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}

	@Test
	public void changePassword() throws Exception{
		String result = this.mockMvc.perform(put("/changePassword").contentType(MediaType.APPLICATION_JSON)
				.param("passwd","12345678")
				.param("newPassword","123456789")
				.param("confirmPassword","123456789")
				)
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}

	@Test
	public void getUserInformation() throws Exception{
		String result = this.mockMvc.perform(get("/getUserInformation").contentType(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}
}