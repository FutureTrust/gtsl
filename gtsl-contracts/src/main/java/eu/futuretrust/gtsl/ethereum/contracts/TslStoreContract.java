/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.ethereum.contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class TslStoreContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051602080610c318339810160405280805160028054919350909150610039906001830161008b565b600080805260016020527fa6eef7e35abe7026729641147f7915573c7e97b47efa546f5f6e3230263bcb49919091558054600160a060020a031916600160a060020a039290921691909117905561014d565b8154818355818115116100b7576002028160020283600052602060002091820191016100b791906100bc565b505050565b6100e991905b808211156100e55760008082556100dc60018301826100ec565b506002016100c2565b5090565b90565b50805460018160011615610100020316600290046000825580601f106101125750610130565b601f0160209004906000526020600020908101906101309190610133565b50565b6100e991905b808211156100e55760008155600101610139565b610ad58061015c6000396000f3006060604052600436106100695763ffffffff60e060020a6000350416631f7b6d32811461006e5780632b5216dd1461009357806338a699a41461012757806356bd079e14610151578063b186ca9714610167578063c9366055146101bf578063da8e187614610215575b600080fd5b341561007957600080fd5b61008161022b565b60405190815260200160405180910390f35b341561009e57600080fd5b6100a9600435610236565b60405182815260406020820181815290820183818151815260200191508051906020019080838360005b838110156100eb5780820151838201526020016100d3565b50505050905090810190601f1680156101185780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b341561013257600080fd5b61013d600435610312565b604051901515815260200160405180910390f35b341561015c57600080fd5b6100a9600435610326565b341561017257600080fd5b6101bd600480359060446024803590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061037d95505050505050565b005b34156101ca57600080fd5b6101bd600480359060446024803590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061051895505050505050565b341561022057600080fd5b6101bd60043561069b565b600254600019015b90565b60006102406108e9565b60008381526001602052604090205460028054919350908390811061026157fe5b90600052602060002090600202016001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103065780601f106102db57610100808354040283529160200191610306565b820191906000526020600020905b8154815290600101906020018083116102e957829003601f168201915b50505050509050915091565b600090815260016020526040902054151590565b60006103306108e9565b826000108015610341575060025483105b151561034c57600080fd5b600280548490811061035a57fe5b906000526020600020906002020160000154915060028381548110151561026157fe5b6000828152600160205260409020548290151561039957600080fd5b6000548390600160a060020a0316638128b897338360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401602060405180830381600087803b15156103f157600080fd5b5af115156103fe57600080fd5b50505060405180519050151561041357600080fd5b8251151561042057600080fd5b60008481526001602052604090205460028054859290811061043e57fe5b90600052602060002090600202016001019080516104609291602001906108fb565b507fe59b36c6c00814bf72042369368079de723dfc0502f4211de1683167c0f214c7848433604051838152600160a060020a038216604082015260606020820181815290820184818151815260200191508051906020019080838360005b838110156104d65780820151838201526020016104be565b50505050905090810190601f1680156105035780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a150505050565b60008281526001602052604090205482901561053357600080fd5b6000548390600160a060020a0316638128b897338360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401602060405180830381600087803b151561058b57600080fd5b5af1151561059857600080fd5b5050506040518051905015156105ad57600080fd5b825115156105ba57600080fd5b60028054600086815260016020819052604090912082905581016105de8382610979565b916000526020600020906002020160006040805190810160405287815260208101879052919050815181556020820151816001019080516106239291602001906108fb565b505050507ff9e6ef992a9df5b346ef0dc29d80d5468f76a113c674b32be557cca61ecf9ddc848433604051838152600160a060020a03821660408201526060602082018181529082018481815181526020019150805190602001908083836000838110156104d65780820151838201526020016104be565b6000818152600160205260408120548190839015156106b957600080fd5b6000548490600160a060020a0316638128b897338360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401602060405180830381600087803b151561071157600080fd5b5af1151561071e57600080fd5b50505060405180519050151561073357600080fd5b600085815260016020819052604090912054600380549196509181016107598382610979565b9160005260206000209060020201600060028781548110151561077857fe5b600091825260209091206002918202018054845560018082018054929594506107b693828601939192610100908316150260001901909116046109aa565b505050508392505b600254600019018310156108435760028054600185019081106107dd57fe5b90600052602060002090600202016002848154811015156107fa57fe5b6000918252602090912082546002928302909101908155600180840180549293610835938386019361010090821615026000190116046109aa565b5050600190930192506107be565b60028054600019810190811061085557fe5b600091825260208220600290910201818155906108756001830182610a1f565b5050600280549061088a906000198301610979565b50600085815260016020526040808220919091557f7c17d64597e483b34a844d191fee3223dcb60756f76c57048ad52d44cc42c400908690339051918252600160a060020a031660208201526040908101905180910390a15050505050565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061093c57805160ff1916838001178555610969565b82800160010185558215610969579182015b8281111561096957825182559160200191906001019061094e565b50610975929150610a66565b5090565b8154818355818115116109a5576002028160020283600052602060002091820191016109a59190610a80565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106109e35780548555610969565b8280016001018555821561096957600052602060002091601f016020900482015b82811115610969578254825591600101919060010190610a04565b50805460018160011615610100020316600290046000825580601f10610a455750610a63565b601f016020900490600052602060002090810190610a639190610a66565b50565b61023391905b808211156109755760008155600101610a6c565b61023391905b80821115610975576000808255610aa06001830182610a1f565b50600201610a865600a165627a7a72305820da7c951d1dcdae06f8928647c2408cf4dae0014843b7ebaa7725bd0e2ebb7c0f0029";

    protected TslStoreContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TslStoreContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TslAddingEventResponse> getTslAddingEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TslAdding", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TslAddingEventResponse> responses = new ArrayList<TslAddingEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TslAddingEventResponse typedResponse = new TslAddingEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TslAddingEventResponse> tslAddingEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TslAdding", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TslAddingEventResponse>() {
            @Override
            public TslAddingEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TslAddingEventResponse typedResponse = new TslAddingEventResponse();
                typedResponse.log = log;
                typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.user = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<TslUpdatingEventResponse> getTslUpdatingEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TslUpdating", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TslUpdatingEventResponse> responses = new ArrayList<TslUpdatingEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TslUpdatingEventResponse typedResponse = new TslUpdatingEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TslUpdatingEventResponse> tslUpdatingEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TslUpdating", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TslUpdatingEventResponse>() {
            @Override
            public TslUpdatingEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TslUpdatingEventResponse typedResponse = new TslUpdatingEventResponse();
                typedResponse.log = log;
                typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.user = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<TslRemovingEventResponse> getTslRemovingEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TslRemoving", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TslRemovingEventResponse> responses = new ArrayList<TslRemovingEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TslRemovingEventResponse typedResponse = new TslRemovingEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TslRemovingEventResponse> tslRemovingEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TslRemoving", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TslRemovingEventResponse>() {
            @Override
            public TslRemovingEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TslRemovingEventResponse typedResponse = new TslRemovingEventResponse();
                typedResponse.log = log;
                typedResponse.code = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> length() {
        final Function function = new Function("length", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple2<BigInteger, byte[]>> getTsl(byte[] _code) {
        final Function function = new Function("getTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_code)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteCall<Tuple2<BigInteger, byte[]>>(
                new Callable<Tuple2<BigInteger, byte[]>>() {
                    @Override
                    public Tuple2<BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<Boolean> exists(byte[] _code) {
        final Function function = new Function("exists", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_code)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Tuple2<byte[], byte[]>> getTsl(BigInteger _index) {
        final Function function = new Function("getTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteCall<Tuple2<byte[], byte[]>>(
                new Callable<Tuple2<byte[], byte[]>>() {
                    @Override
                    public Tuple2<byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<byte[], byte[]>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> updateTsl(byte[] _code, byte[] _hash) {
        final Function function = new Function(
                "updateTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_code), 
                new org.web3j.abi.datatypes.DynamicBytes(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addTsl(byte[] _code, byte[] _hash) {
        final Function function = new Function(
                "addTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_code), 
                new org.web3j.abi.datatypes.DynamicBytes(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeTsl(byte[] _code) {
        final Function function = new Function(
                "removeTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_code)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<TslStoreContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String userOracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userOracleAddress)));
        return deployRemoteCall(TslStoreContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<TslStoreContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String userOracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userOracleAddress)));
        return deployRemoteCall(TslStoreContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static TslStoreContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TslStoreContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static TslStoreContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TslStoreContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TslAddingEventResponse {
        public Log log;

        public byte[] code;

        public byte[] hash;

        public String user;
    }

    public static class TslUpdatingEventResponse {
        public Log log;

        public byte[] code;

        public byte[] hash;

        public String user;
    }

    public static class TslRemovingEventResponse {
        public Log log;

        public byte[] code;

        public String user;
    }
}
