package com.github.chen0040.rl.learning.utils;

//import com.alibaba.fastjson.JSON;
import com.github.chen0040.rl.utils.Vec;
import com.google.gson.Gson;

import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class VecUnitTest {
	@Test
	public void testJsonSerialization() {
		Vec vec = new Vec(100);
		vec.set(9, 100);
		vec.set(11, 2);
		vec.set(0, 1);
		String json = new Gson().toJson(vec); //JSON.toJSONString(vec);
		Vec vec2 = new Gson().fromJson(json, Vec.class); //JSON.parseObject(json, Vec.class);
		assertThat(vec).isEqualTo(vec2);
	}
}
