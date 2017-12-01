package com.github.chen0040.rl.learning.models;

import com.alibaba.fastjson.JSON;
import com.github.chen0040.rl.models.QModel;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class QModelUnitTest {
    @Test
    public void testJsonSerialization() {
        QModel model = new QModel(100, 10);

        model.setQ(3, 4, 0.3);
        model.setQ(92, 2, 0.2);

        model.setAlpha(0.4);
        model.setGamma(0.3);

        String json = JSON.toJSONString(model);
        QModel model2 = JSON.parseObject(json, QModel.class);

        assertThat(model).isEqualTo(model2);
        assertThat(model.getQ()).isEqualTo(model2.getQ());
        assertThat(model.getAlpha()).isEqualTo(model2.getAlpha());
        assertThat(model.getStateCount()).isEqualTo(model2.getStateCount());
        assertThat(model.getActionCount()).isEqualTo(model2.getActionCount());
        assertThat(model.getGamma()).isEqualTo(model2.getGamma());




    }
}
