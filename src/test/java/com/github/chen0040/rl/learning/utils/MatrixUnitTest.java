package com.github.chen0040.rl.learning.utils;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.rl.utils.Matrix;
import com.google.gson.Gson;

import org.testng.annotations.Test;

import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MatrixUnitTest {

	private static final Random random = new Random(42);

	@Test
	public void testJsonSerialization() {
		Matrix matrix = new Matrix(10, 10);
		matrix.set(0, 0, 10);
		matrix.set(4, 2, 2);
		matrix.set(3, 3, 2);

		assertThat(matrix.get(0, 0)).isEqualTo(10);
		assertThat(matrix.get(4, 2)).isEqualTo(2);
		assertThat(matrix.get(3, 3)).isEqualTo(2);
		assertThat(matrix.get(4, 4)).isEqualTo(0);

		assertThat(matrix.getRowCount()).isEqualTo(10);
		assertThat(matrix.getColumnCount()).isEqualTo(10);

		String json = new Gson().toJson(matrix); //JSON.toJSONString(matrix, SerializerFeature.PrettyFormat);

		System.out.println(json);
		Matrix matrix2 = new Gson().fromJson(json, Matrix.class); //JSON.parseObject(json, Matrix.class);
		assertThat(matrix).isEqualTo(matrix2);

		for (int i = 0; i < matrix.getRowCount(); ++i) {
			for (int j = 0; j < matrix.getColumnCount(); ++j) {
				assertThat(matrix.get(i, j)).isEqualTo(matrix2.get(i, j));
			}
		}
	}

	@Test
	public void testJsonSerialization_Random() {
		Matrix matrix = new Matrix(10, 10);
		for (int i = 0; i < matrix.getRowCount(); ++i) {
			for (int j = 0; j < matrix.getColumnCount(); ++j) {
				matrix.set(i, j, random.nextDouble());
			}
		}
		Matrix matrix2 = matrix.makeCopy();
		assertThat(matrix).isEqualTo(matrix2);

		String json = new Gson().toJson(matrix); //JSON.toJSONString(matrix);
		Matrix matrix3 = new Gson().fromJson(json, Matrix.class); //JSON.parseObject(json, Matrix.class);
		assertThat(matrix2).isEqualTo(matrix3);

		for (int i = 0; i < matrix.getRowCount(); ++i) {
			for (int j = 0; j < matrix.getColumnCount(); ++j) {
				assertThat(matrix2.get(i, j)).isEqualTo(matrix3.get(i, j));
			}
		}
	}
}
