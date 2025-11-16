import model.RandomIntListGenerator;

import java.util.List;

public class AlgorithmAnalysisDriver {

    public static void main(String[] args) {
        RandomIntListGenerator generator = new RandomIntListGenerator(99); // 0..98 like before

        List<Integer> listSize10   = generator.generate(10);
        List<Integer> listSize100  = generator.generate(100);
        List<Integer> listSize1000 = generator.generate(1000);

    }

}
