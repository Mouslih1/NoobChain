package org.example.entities;

import org.example.utils.StringUtil;

import java.util.Date;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private Long timeStamp;

    public Block(String data, String previousHash)
    {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash()
    {
        return StringUtil.applySha256
                (
                  previousHash + Long.toString(timeStamp) + data
                );
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}