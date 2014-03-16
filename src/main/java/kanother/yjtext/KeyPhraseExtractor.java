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

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Yahoo!キーフレーズ抽出APIの実行を行うクラスです．
 * @author kanazawa
 * @version 0.0.1
 */
public final class KeyPhraseExtractor implements AutoCloseable {

    private static final String API_URL =
            "http://jlp.yahooapis.jp/KeyphraseService/V1/extract";

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final CloseableHttpClient client;

    private final String applicationId;

    private KeyPhraseExtractor(final String applicationId) {
        client = HttpClientBuilder.create().build();
        this.applicationId = applicationId;
    }

    public static KeyPhraseExtractor create(final String applicationId) {
        return new KeyPhraseExtractor(applicationId);
    }

    public List<KeyPhrase> extract(final String sentence) throws IOException {
        final List<KeyPhrase> result = new ArrayList<>();
        final HttpPost post = new HttpPost(API_URL);
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", applicationId));
        params.add(new BasicNameValuePair("sentence", sentence));
        params.add(new BasicNameValuePair("output", "json"));
        post.setEntity(new UrlEncodedFormEntity(params, UTF8));
        try (final CloseableHttpResponse response = client.execute(post)) {
            JsonParser parser = new JsonFactory()
                    .createJsonParser(response.getEntity().getContent());
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                if (parser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    final String value = parser.getText();
                    parser.nextToken();
                    final int score = parser.getIntValue();
                    result.add(new KeyPhrase(value, score));
                }
            }
        }
        post.abort();
        return result;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
