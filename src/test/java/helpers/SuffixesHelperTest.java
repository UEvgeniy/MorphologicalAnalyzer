package helpers;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

/**
 * Testing helpers
 */
public class SuffixesHelperTest {

    /**
     * @return Collection of
     *    {First str,
     *    second str,
     *    their common prefix,
     *    their common suffix,
     *    their differing suffix for the 1st word}
     */
    @DataProvider
    public Object[][] wordsDataProvider(){
        return new Object[][]{
                {"слово", "слова", 4, 0, 1},
                {"", "слово", 0, 0, 0},
                {"строка", "подстрока", 0, 6, 6},
                {null, "", -1, -1, -1},
                {"", "", 0, 0, 0},
                {"начинать", "начинающий", 6, 0, 2},
                {"начинающий", "начинать", 6, 0, 4},
        };
    }

    @Test(dataProvider = "wordsDataProvider")
    public void testGetCommonPrefixLength(String str1, String str2, int pref, int suf, int dif) throws Exception {

        if (str1 == null || str2 == null){
            assertThrows(
                    NullPointerException.class,
                    () ->  SuffixesHelper.getCommonPrefixLength(str1, str2)
            );
            return;
        }

        assertEquals( SuffixesHelper.getCommonPrefixLength(str1, str2), pref);
    }

    @Test(dataProvider = "wordsDataProvider")
    public void testGetCommonSuffixesLength(String str1, String str2, int pref, int suf, int dif) throws Exception {

        if (str1 == null || str2 == null){
            assertThrows(
                    NullPointerException.class,
                    () ->  SuffixesHelper.getCommonSuffixesLength(str1, str2)
            );
            return;
        }

        assertEquals( SuffixesHelper.getCommonSuffixesLength(str1, str2), suf);
    }

    @Test(dataProvider = "wordsDataProvider")
    public void testGetDifferingSuffixesLength(String str1, String str2, int pref, int suf, int dif)
            throws Exception {

        if (str1 == null || str2 == null){
            assertThrows(
                    NullPointerException.class,
                    () ->  SuffixesHelper.getDifferingSuffixesLength(str1, str2)
            );
            return;
        }

        assertEquals( SuffixesHelper.getDifferingSuffixesLength(str1, str2), dif);
    }
}