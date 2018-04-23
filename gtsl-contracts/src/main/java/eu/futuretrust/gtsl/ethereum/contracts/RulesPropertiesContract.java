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
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class RulesPropertiesContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516020806104728339810160405280805160008054600160a060020a03909216600160a060020a0319909216919091179055505061041e806100546000396000f30060606040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416636d4ce63c8114610050578063c43ed2c8146100da575b600080fd5b341561005b57600080fd5b61006361012d565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561009f578082015183820152602001610087565b50505050905090810190601f1680156100cc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156100e557600080fd5b61012b60046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506101d695505050505050565b005b610135610348565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101cb5780601f106101a0576101008083540402835291602001916101cb565b820191906000526020600020905b8154815290600101906020018083116101ae57829003601f168201915b505050505090505b90565b60005473ffffffffffffffffffffffffffffffffffffffff1663fe9fbb80336040517c010000000000000000000000000000000000000000000000000000000063ffffffff841602815273ffffffffffffffffffffffffffffffffffffffff9091166004820152602401602060405180830381600087803b151561025957600080fd5b5af1151561026657600080fd5b50505060405180519050151561027b57600080fd5b600181805161028e92916020019061035a565b507ff85c254b5510ca09c9badc81fd2b48ca195bb97efd5422dcd4846f2e7a67ef5d813360405173ffffffffffffffffffffffffffffffffffffffff8216602082015260408082528190810184818151815260200191508051906020019080838360005b8381101561030a5780820151838201526020016102f2565b50505050905090810190601f1680156103375780820380516001836020036101000a031916815260200191505b50935050505060405180910390a150565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061039b57805160ff19168380011785556103c8565b828001600101855582156103c8579182015b828111156103c85782518255916020019190600101906103ad565b506103d49291506103d8565b5090565b6101d391905b808211156103d457600081556001016103de5600a165627a7a72305820c314b22d72993c5763b361f61c8c203cc71f2c520c989eaf38b317fcfb46642b0029";

    protected RulesPropertiesContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RulesPropertiesContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<UpdatedEventResponse> getUpdatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Updated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<UpdatedEventResponse> responses = new ArrayList<UpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdatedEventResponse typedResponse = new UpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UpdatedEventResponse> updatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Updated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, UpdatedEventResponse>() {
            @Override
            public UpdatedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                UpdatedEventResponse typedResponse = new UpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<byte[]> get() {
        final Function function = new Function("get", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> update(byte[] _hash) {
        final Function function = new Function(
                "update", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<RulesPropertiesContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String userOracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userOracleAddress)));
        return deployRemoteCall(RulesPropertiesContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<RulesPropertiesContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String userOracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userOracleAddress)));
        return deployRemoteCall(RulesPropertiesContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RulesPropertiesContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RulesPropertiesContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static RulesPropertiesContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RulesPropertiesContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class UpdatedEventResponse {
        public Log log;

        public byte[] hash;

        public String user;
    }
}
