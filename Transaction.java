package org.example;


import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

class Block {
    public int index;
    public String pre_hash;
    public long timestamp;
    public List<Transaction> transactions;
    public String secret_info;
    public int nonce;
    public String hash;




}





class Transaction {
    public String from;
    public String to;
    public double value;
}


public class Main {
    public static void main(String[] args) throws IOException {
        Path directoryPath = Paths.get("D:\\IntelliJ IDEA\\iuu\\src\\main\\java\\l\\transactions\\transactions");
        List<Block> blocks = readBlocksFromDirectory(directoryPath);
        blocks.sort(Comparator.comparingInt(b -> b.index));
        Block specialHashBlock = findBlockWithSpecialHash(blocks);
        if (specialHashBlock != null) {
            System.out.println("автор и номер блока с хэшем 0x000...000: " + specialHashBlock.index);
        }




        analyzeForks(blocks);
        analyzeRewards(blocks);
        System.out.println(specialHashBlock.transactions.get(2).to);
        String secretInfo = collectSecretInfo(blocks);
        System.out.println("secret_info: " + secretInfo);
        String flag = convertToFlagFormat(secretInfo);
        System.out.println("WSflag: " + flag);

    }



    private static List<Block> readBlocksFromDirectory(Path directoryPath) throws IOException {
        List<Block> blocks = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path entry : stream) {
                byte[] content = Files.readAllBytes(entry);
                Block block = mapper.readValue(content, Block.class);
                blocks.add(block);
            }
        }
        return blocks;
    }

    private static Block findBlockWithSpecialHash(List<Block> blocks) {
        String targetHashPrefix = "000";
        String endtargetHashPrefix = "0000";
        return blocks.stream()
                .filter(block -> block.hash.startsWith(targetHashPrefix))
                .filter(block -> block.hash.endsWith(endtargetHashPrefix))
                .findFirst()
                .orElse(null);
    }

    private static void analyzeForks(List<Block> blocks) {

    }

    private static void analyzeRewards(List<Block> blocks) {

    }





    private static String collectSecretInfo(List<Block> blocks) {
        return blocks.stream()
                .filter(block -> !block.secret_info.isEmpty())
                .map(block -> block.secret_info)
                .collect(Collectors.joining());
    }
    private static String convertToFlagFormat(String hexValue) {
        StringBuilder flag = new StringBuilder("WSflag{");
        for (int i = 0; i < hexValue.length(); i += 2) {
            String str = hexValue.substring(i, i + 2);
            flag.append((char) Integer.parseInt(str, 16));
        }
        flag.append("}");
        return flag.toString();
    }
}
