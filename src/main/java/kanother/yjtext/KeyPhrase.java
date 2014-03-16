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

/**
 * Yahoo!キーフレーズ抽出APIにより抽出されたキーフレーズを保持するクラスです．<br/>
 * このクラスはImmutableであり，スレッドセーフです．
 * @author kanazawa
 * @version 0.0.1
 */
public class KeyPhrase implements Comparable<KeyPhrase> {

    /** キーフレーズ文字列 */
    private final String value;

    /** 抽出されたキーフレーズの重要度(100を最大とする相対値) */
    private final int score;

    /**
     * キーフレーズ文字列と重要度の数値を指定し，インスタンスを初期化します．
     * @param value キーフレーズ文字列
     * @param score 重要度
     */
    public KeyPhrase(final String value, final int score) {
        this.value = value;
        this.score = score;
    }

    /**
     * キーフレーズ文字列を返します．
     * @return キーフレーズ文字列
     */
    public String getValue() {
        return value;
    }

    /**
     * 抽出されたキーフレーズの重要度を返します．
     * @return 重要度
     */
    public int getScore() {
        return score;
    }

    /**
     * このインスタンスの文字列形式表現を以下の形式で返します．<br/>
     * [キーフレーズ文字列, 重要度]
     * @return 文字列形式表現
     */
    @Override
    public String toString() {
        return String.format("[%s, %d]", value, score);
    }

    /**
     * このキーフレーズと他のキーフレーズを比較します．<br/>
     * このキーフレーズの重要度の方が大きい場合は正の数値，
     * このキーフレーズの重要度の方が小さい場合は負の数値，
     * 重要度が等しい場合はキーフレーズ文字列の自然順序を比較した結果，
     * キーフレーズ文字列も等しい場合は0を返します．
     * 引数としてnullが指定された場合，NullPointerExceptionをスローします．
     * @param another 比較対象のキーフレーズ
     * @return 比較結果
     */
    @Override
    public int compareTo(final KeyPhrase another) {
        if (another == null) {
            throw new NullPointerException();
        } else if (score == another.getScore()) {
            return value.compareTo(another.getValue());
        } else {
            return score - another.getScore();
        }
    }

    /**
     * このキーフレーズと他のオブジェクトの同一性を比較します．
     * 引数で指定されたオブジェクトが{@link KeyPhrase}であり，
     * このキーフレーズと等しいキーフレーズ文字列と重要度を持つ場合trueを，
     * そうでない場合はfalseを返します．
     * @param anotherObject 比較対象のオブジェクト
     * @return 比較結果
     */
    @Override
    public boolean equals(Object anotherObject) {
        if (this == anotherObject) {
            return true;
        } else if (anotherObject instanceof KeyPhrase) {
            final KeyPhrase another = (KeyPhrase) anotherObject;
            return value.equals(another.getValue())
                    && score == another.getScore();
        } else {
            return false;
        }
    }

    /**
     * このインスタンスのハッシュコードを返します．<br/>
     * {@link KeyPhrase#equals(Object)}がtrueを返す場合，
     * このメソッドの結果も等しい数値を返します．
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return (23 + value.hashCode()) * 37 + score;
    }
}
