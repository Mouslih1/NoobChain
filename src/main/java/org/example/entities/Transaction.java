package org.example.entities;

import org.example.NoobChain;
import org.example.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    private String transactionId;
    private PublicKey sender;
    private PublicKey reciepient;
    private float value;
    private byte[] signature;

    private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs)
    {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calulateHash()
    {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey)
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) +
                Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    public boolean verifiySignature()
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) +
                Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {

        if(!verifiySignature())
        {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        for(TransactionInput i : inputs) {
            i.setUTXO(NoobChain.UTXOs.get(i.getTransactionOutputId()));
        }

        if(getInputsValue() < NoobChain.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

        for(TransactionOutput o : outputs) {
            NoobChain.UTXOs.put(o.id , o);
        }

        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue;
            NoobChain.UTXOs.remove(i.getUTXO().id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null) continue; //if Transaction can't be found skip it
            total += i.getUTXO().value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

















    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutputs(ArrayList<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public void setInputs(ArrayList<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setReciepient(PublicKey reciepient) {
        this.reciepient = reciepient;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public ArrayList<TransactionOutput> getOutputs() {
        return outputs;
    }

    public ArrayList<TransactionInput> getInputs() {
        return inputs;
    }

    public byte[] getSignature() {
        return signature;
    }

    public float getValue() {
        return value;
    }

    public PublicKey getReciepient() {
        return reciepient;
    }

    public PublicKey getSender() {
        return sender;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
