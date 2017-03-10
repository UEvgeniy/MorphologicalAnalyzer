package rule_applicability_reg;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Test N-gram divider
 */
public class NGramsTest {
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
                {"слово", 2},
                {"длинношеее", 2},
                {"меньше", 7},
                {"равно", 5},
                {"исключение", 0},
                {"еще одно исключение", -10}
        };
    }

    @Test(dataProvider = "wordsDataProvider")
    public void testGet(String word, int N) throws Exception {

        if (N <= 0){
            assertThrows(
                    IllegalArgumentException.class,
                    () ->  NGrams.get(word, N)
            );
            return;
        }


        // todo make test
        /*List<String> result = NGrams.get(word, N);

        if (word.length() <= N){
            assertEquals(result.size(), 1);
            assertTrue(result.contains(word));
        }
        else{
           assertEquals(result.size(), word.length() - N + 1);

           for (int i = 0; i < word.length() - N + 1; i++){
               assertTrue(result.contains(word.substring(i, i + N)));
           }
        }
        */

    }

}