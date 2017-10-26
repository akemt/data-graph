package com.beyond.algo.algoconsoleboot.controller;

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


		String result = this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
				.param("usrName","xiaoxx")
				.param("passwd","123456")
				.param("email","test@qq.com"))
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

		String result = this.mockMvc.perform(post("/user/updateUserInformation").contentType(MediaType.APPLICATION_JSON)
				.param("usrSn","b468d46913c845e780cabf1b9fa0f0fb")
				.param("usrName","传智")
				.param("email","zhangchuanzhi@126.com")
				.param("needNotify","1")
				.param("telephone","13252998247")
				.param("usrUrl","https://www.23245.com"))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}
	@Test
	public void accountInformation() throws Exception{

		String result = this.mockMvc.perform(get("/user/accountInformation").contentType(MediaType.APPLICATION_JSON)
				.param("accSn","aac44b648b10429cbaf85a6ae0f61165"))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}
}
