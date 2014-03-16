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
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

/**
 * {@link KeyPhrase}の単体テストケースです．
 */
public class KeyPhraseTest {

    /**
     * {@link KeyPhrase#KeyPhrase(String, int)}によるインスタンス生成をテストします．
     */
    @Test
    public void インスタンス生成() {
        final KeyPhrase keyPhrase1 = new KeyPhrase("テスト1", 100);
        final KeyPhrase keyPhrase2 = new KeyPhrase("テスト2", 50);
        final KeyPhrase keyPhrase3 = new KeyPhrase("テスト3", 0);
        assertThat("Instance should not be null", keyPhrase1, notNullValue());
        assertThat("Value should be equal to 'テスト1'",
                keyPhrase1.getValue(), is("テスト1"));
        assertThat("Score should be equal to 100",
                keyPhrase1.getScore(), is(100));
        assertThat("Instance should not be null", keyPhrase2, notNullValue());
        assertThat("Value should be equal to 'テスト2'",
                keyPhrase2.getValue(), is("テスト2"));
        assertThat("Score should be equal to 50",
                keyPhrase2.getScore(), is(50));
        assertThat("Instance should not be null", keyPhrase3, notNullValue());
        assertThat("Value should be equal to 'テスト3'",
                keyPhrase3.getValue(), is("テスト3"));
        assertThat("Score should be equal to 100",
                keyPhrase3.getScore(), is(0));
    }

    /**
     * {@link KeyPhrase#toString()}による文字列形式表現をテストします．
     */
    @Test
    public void 文字列形式表現() {
        final KeyPhrase keyPhrase = new KeyPhrase("テスト", 50);
        assertThat("Value should be equal to '[テスト, 50]'",
                keyPhrase.toString(), is("[テスト, 50]"));
    }

    /**
     * {@link KeyPhrase#compareTo(KeyPhrase)}を用いた順序比較をテストします．
     */
    @Test
    public void 順序比較() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        assertThat("Compare result should be equal to 0",
                keyPhrase.compareTo(keyPhrase), is(0));
        assertThat("Compare result should be equal to 0",
                keyPhrase.compareTo(new KeyPhrase("test", 50)), is(0));
        assertThat("Compare result should be greater than 0",
                keyPhrase.compareTo(new KeyPhrase("test", 49)),
                greaterThan(0));
        assertThat("Compare result should be less than 0",
                keyPhrase.compareTo(new KeyPhrase("test", 51)),
                lessThan(0));
        assertThat("Compare result should be greater than 0",
                keyPhrase.compareTo(new KeyPhrase("tess", 50)),
                greaterThan(0));
        assertThat("Compare result should be less than 0",
                keyPhrase.compareTo(new KeyPhrase("test_", 50)),
                lessThan(0));
        try {
            keyPhrase.compareTo(null);
            fail("Compare should fail");
        } catch (Exception e) {
            assertThat("Error class should be equal to NullPointerException",
                    e.getClass().getSimpleName(), is("NullPointerException"));
        }
    }

    /**
     * {@link KeyPhrase#equals(Object)}を用いた同一性の比較をテストします．
     */
    @Test
    public void 同一性比較() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        assertThat("Another object should be equal to keyPhase",
                keyPhrase.equals(keyPhrase), is(true));
        assertThat("Another object should be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("test", 50)), is(true));
        assertThat("Another object should not be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("test", 49)), is(false));
        assertThat("Another object should not be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("tess", 50)), is(false));
        assertThat("Another object should not be equal to keyPhase",
                keyPhrase.equals(new Object()), is(false));
        //noinspection ObjectEqualsNull
        assertThat("Another object should not be equal to keyPhase",
                keyPhrase.equals(null), is(false));
    }

    /**
     * {@link KeyPhrase#hashCode()}を用いたハッシュ値の算出をテストします．
     */
    @Test
    public void ハッシュコード() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        assertThat("Hash code should be equal to 131591327",
                keyPhrase.hashCode(), is(131591327));
    }
}
