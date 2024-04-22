package org.example;

import com.google.gson.GsonBuilder;
import org.example.entities.Block;

import java.util.ArrayList;

public class Main {
    public static ArrayList<Block> chainBlock = new ArrayList<>();

    public static void main(String[] args) {
        chainBlock.add(new Block("Hi im the first block", "0"));
        chainBlock.add(new Block("Yo im the second block",chainBlock.get(chainBlock.size() - 1).getHash()));
        chainBlock.add(new Block("Hey im the third block",chainBlock.get(chainBlock.size()-1).getHash()));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(chainBlock);
        System.out.println(blockchainJson);
    }
}