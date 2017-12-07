import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Part1 {

    private static final String BLOCK_PATH = "./blocks.txt";

    private static List<Integer> getBlocks(String line) {
        String[] blockStrings = line.split("\\s+");

        List<Integer> blocks = Arrays.stream(blockStrings).map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        return blocks;
    }

    private static List<Integer> distributeBlocks(List<Integer> blocks) {
        Integer max = 0;
        Integer maxIndex = 0;
        for (int i = 0; i< blocks.size(); i++) {
            if (blocks.get(i) > max) {
                max = blocks.get(i);
                maxIndex = i;
            }
        }

        List<Integer> newBlocks = new ArrayList<Integer>(blocks);
        //remove all blocks from bank with max
        newBlocks.set(maxIndex, 0);

        Integer next = (maxIndex + 1) % blocks.size();
        Integer blocksToDistribute = max;
        while (blocksToDistribute > 0) {
            Integer val = newBlocks.get(next);
            val++;
            newBlocks.set(next, val);
            blocksToDistribute--;
            next = (next + 1) % blocks.size();
        }

        return newBlocks;
    }

    private static void findLoop(List<Integer> blocks) {
        List<List<Integer>> states = new ArrayList<List<Integer>>();

        List<Integer> latestState = blocks;
        Integer stepCount = 0;

        while (!states.contains(latestState)) {
            states.add(latestState);
            latestState = Part1.distributeBlocks(latestState);
            stepCount++;
        }

        Integer loopLength = stepCount - states.indexOf(latestState);

        System.out.println(String.format("Number of steps before finding loop: %d", stepCount));
        System.out.println(String.format("Loop cycle length: %d", loopLength));
    }

    public static void main(String[] args) {
        Path p = Paths.get(BLOCK_PATH);
        try (InputStream in = Files.newInputStream(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){

            String line = reader.readLine();
            
            List<Integer> blocks = Part1.getBlocks(line);

            Part1.findLoop(blocks);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}