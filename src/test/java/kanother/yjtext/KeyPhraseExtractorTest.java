/*
 * Copyright 2014 Hidetomo Kanazawa (https://github.com/kanother)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kanother.yjtext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import mockit.Deencapsulation;
import mockit.FullVerificationsInOrder;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * {@link KeyPhraseExtractor}の単体テストケースです．
 */
public class KeyPhraseExtractorTest {

    /** {@link HttpClientBuilder}のMockオブジェクト */
    @Mocked
    @SuppressWarnings("unused")
    private HttpClientBuilder mockClientBuilder;

    /** {@link CloseableHttpClient}のMockオブジェクト */
    @Mocked
    @SuppressWarnings("unused")
    private CloseableHttpClient mockClient;

    /** {@link CloseableHttpResponse}のMockオブジェクト */
    @Mocked
    @SuppressWarnings("unused")
    private CloseableHttpResponse mockResponse;

    /**
     * {@link KeyPhraseExtractor#create(String)}を用いたインスタンス生成をテストします．
     * @throws IOException テストに失敗した場合
     */
    @Test
    public void インスタンス生成() throws IOException {
        new NonStrictExpectations() {
            {
                HttpClientBuilder.create();
                result = mockClientBuilder;
                mockClientBuilder.build();
                result = mockClient;
                mockClient.close();
                result = null;
            }
        };
        final KeyPhraseExtractor extractor =
                KeyPhraseExtractor.create("appId");
        assertThat("Instance should not be null", extractor, notNullValue());
        assertThat("Application id should be equal to 'appId'",
                Deencapsulation.<String>getField(extractor, "applicationId"),
                is("appId"));
        extractor.close();
        new FullVerificationsInOrder() {
            {
                HttpClientBuilder.create();
                times = 1;
                mockClientBuilder.build();
                times = 1;
                mockClient.close();
                times = 1;
            }
        };
    }

    /**
     * {@link KeyPhraseExtractor#extract(String)}を用いたキーフレーズ抽出をテストします．
     * @throws Exception テストに失敗した場合
     */
    @Test
    public void キーフレーズ抽出() throws Exception {
        final String mockResult =
                "{\"phrase1\":100,\"phrase2\":80,\"phrase3\":50}";
        try (final InputStream stream = new ByteArrayInputStream(
                mockResult.getBytes())) {
            final BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContent(stream);
            new NonStrictExpectations() {
                {
                    HttpClientBuilder.create();
                    result = mockClientBuilder;
                    mockClientBuilder.build();
                    result = mockClient;
                }
                {
                    mockClient.execute((HttpPost) any);
                    result = mockResponse;
                    mockResponse.getEntity();
                    result = entity;
                }
            };
            try (final KeyPhraseExtractor extractor =
                         KeyPhraseExtractor.create("appId")) {
                final List<KeyPhrase> result =
                        extractor.extract("Test sentence");
                assertThat("Result size should be equal to 3",
                        result.size(), is(3));
                final KeyPhrase result1 = result.get(0);
                assertThat("1st key phrase should be equal to 'phrase1'",
                        result1.getValue(), is("phrase1"));
                assertThat("1st key score should be equal to 100",
                        result1.getScore(), is(100));
                final KeyPhrase result2 = result.get(1);
                assertThat("2nd key phrase should be equal to 'phrase2'",
                        result2.getValue(), is("phrase2"));
                assertThat("2nd key score should be equal to 80",
                        result2.getScore(), is(80));
                final KeyPhrase result3 = result.get(2);
                assertThat("3rd key phrase should be equal to 'phrase3'",
                        result3.getValue(), is("phrase3"));
                assertThat("3rd key score should be equal to 50",
                        result3.getScore(), is(50));
            }
            new FullVerificationsInOrder() {
                {
                    HttpPost post;
                    HttpClientBuilder.create();
                    times = 1;
                    mockClientBuilder.build();
                    times = 1;
                    mockClient.execute(post = withCapture());
                    times = 1;
                    assertThat(post, notNullValue());
                    assert post != null;
                    assertThat("POST URI assertion", post.getURI(),
                            is(new URI("http://jlp.yahooapis.jp/KeyphraseService/V1/extract")));
                    assertThat("Post entity assertion",
                            EntityUtils.toString(post.getEntity()),
                            is("appid=appId&sentence=Test+sentence&output=json"));
                    mockResponse.getEntity();
                    times = 1;
                    mockResponse.close();
                    times = 1;
                    mockClient.close();
                    times = 1;
                }
            };
        }
    }
}
