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

import org.junit.Assert;
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
        Assert.assertNotNull("Instance should not be null", keyPhrase1);
        Assert.assertEquals("Value should be equal to 'テスト1'",
                "テスト1", keyPhrase1.getValue());
        Assert.assertEquals("Score should be equal to 100",
                100, keyPhrase1.getScore());
        Assert.assertNotNull("Instance should not be null", keyPhrase2);
        Assert.assertEquals("Value should be equal to 'テスト2'",
                "テスト2", keyPhrase2.getValue());
        Assert.assertEquals("Score should be equal to 50",
                50, keyPhrase2.getScore());
        Assert.assertNotNull("Instance should not be null", keyPhrase3);
        Assert.assertEquals("Value should be equal to 'テスト3'",
                "テスト3", keyPhrase3.getValue());
        Assert.assertEquals("Score should be equal to 0",
                0, keyPhrase3.getScore());
    }

    /**
     * {@link KeyPhrase#toString()}による文字列形式表現をテストします．
     */
    @Test
    public void 文字列形式表現() {
        final KeyPhrase keyPhrase = new KeyPhrase("テスト", 50);
        Assert.assertEquals("Value should be equal to '[テスト, 50]'",
                "[テスト, 50]", keyPhrase.toString());
    }

    /**
     * {@link KeyPhrase#compareTo(KeyPhrase)}を用いた順序比較をテストします．
     */
    @Test
    public void 順序比較() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        Assert.assertEquals("Compare result should be equal to 0",
                0, keyPhrase.compareTo(keyPhrase));
        Assert.assertEquals("Compare result should be equal to 0",
                0, keyPhrase.compareTo(new KeyPhrase("test", 50)));
        Assert.assertTrue("Compare result should be greater than 0",
                keyPhrase.compareTo(new KeyPhrase("test", 49)) > 0);
        Assert.assertTrue("Compare result should be lesser than 0",
                keyPhrase.compareTo(new KeyPhrase("test", 51)) < 0);
        Assert.assertTrue("Compare result should be greater than 0",
                keyPhrase.compareTo(new KeyPhrase("tess", 50)) > 0);
        Assert.assertTrue("Compare result should be lesser than 0",
                keyPhrase.compareTo(new KeyPhrase("test_", 50)) < 0);
        try {
            keyPhrase.compareTo(null);
            Assert.fail("Compare should fail");
        } catch (Exception e) {
            Assert.assertSame(
                    "Error class should be equal to NullPointerException",
                    NullPointerException.class, e.getClass());
        }
    }

    /**
     * {@link KeyPhrase#equals(Object)}を用いた同一性の比較をテストします．
     */
    @Test
    public void 同一性比較() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        Assert.assertTrue("Another object should be equal to keyPhase",
                keyPhrase.equals(keyPhrase));
        Assert.assertTrue("Another object should be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("test", 50)));
        Assert.assertFalse("Another object should not be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("test", 49)));
        Assert.assertFalse("Another object should not be equal to keyPhase",
                keyPhrase.equals(new KeyPhrase("tess", 50)));
        Assert.assertFalse("Another object should not be equal to keyPhase",
                keyPhrase.equals(new Object()));
        //noinspection ObjectEqualsNull
        Assert.assertFalse("Another object should not be equal to keyPhase",
                keyPhrase.equals(null));
    }

    /**
     * {@link KeyPhrase#hashCode()}を用いたハッシュ値の算出をテストします．
     */
    @Test
    public void ハッシュコード() {
        final KeyPhrase keyPhrase = new KeyPhrase("test", 50);
        Assert.assertEquals("Hash code should be equal to 131591327",
                131591327, keyPhrase.hashCode());
    }
}
