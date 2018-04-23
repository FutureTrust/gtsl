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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
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
public class ConsortiumContract extends Contract {
    private static final String BINARY = "606060405234156200001057600080fd5b6040516040806200177d833981016040528080519190602001805191506200004990503382640100000000620010c76200005382021704565b506002556200022a565b6200006c82640100000000620007e46200018782021704565b1580156200009157506200008f82826401000000006200042a620001a882021704565b155b15156200009d57600080fd5b60018054808201620000b08382620001da565b5060009182526020808320919091018054600160a060020a031916600160a060020a0386169081179091556001805491845291839052604092839020600019909101815560028101805460ff19168317905542918101919091556003018290557f144590f0e8efadd724bf6109eaef479b6501fce4a7f9601fac3a6e702dad900c90839051600160a060020a0390911681526040602082018190526005818301527f616464656400000000000000000000000000000000000000000000000000000060608301526080909101905180910390a15050565b600160a060020a031660009081526020819052604090206002015460ff1690565b6000811515620001b757600080fd5b50600160a060020a03919091166000908152602081905260409020600301541490565b81548183558181151162000201576000838152602090206200020191810190830162000206565b505050565b6200022791905b808211156200022357600081556001016200020d565b5090565b90565b611543806200023a6000396000f3006060604052600436106100da5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166308ae4b0c81146100df5780630d50167f1461012c5780630d61b519146101635780632cd046eb1461017b57806357e6c2f41461018e578063761bbd8a146101b55780637e03f8c2146101c85780638128b897146101db578063a03f9c29146101fd578063a509b3f914610210578063c7f758a814610232578063c9d27afe14610296578063cc4e6299146102b1578063da2b17d7146102e3578063fe9fbb80146102f9575b600080fd5b34156100ea57600080fd5b6100fe600160a060020a0360043516610318565b6040519384526020840192909252151560408084019190915260608301919091526080909101905180910390f35b341561013757600080fd5b610151600160a060020a0360043516602435604435610345565b60405190815260200160405180910390f35b341561016e57600080fd5b610179600435610384565b005b341561018657600080fd5b610151610407565b341561019957600080fd5b6101a161040e565b604051901515815260200160405180910390f35b34156101c057600080fd5b61015161041e565b34156101d357600080fd5b610151610424565b34156101e657600080fd5b6101a1600160a060020a036004351660243561042a565b341561020857600080fd5b61015161045b565b341561021b57600080fd5b610151600160a060020a0360043516602435610490565b341561023d57600080fd5b6102486004356104ec565b60405185151581528415156020820152600160a060020a0384166040820152606081018390526080810182600181111561027e57fe5b60ff1681526020019550505050505060405180910390f35b34156102a157600080fd5b61017960043560243515156105d7565b34156102bc57600080fd5b6102c76004356107aa565b604051600160a060020a03909116815260200160405180910390f35b34156102ee57600080fd5b6101a16004356107d2565b341561030457600080fd5b6101a1600160a060020a03600435166107e4565b60006020819052908152604090208054600182015460028301546003909301549192909160ff9091169084565b600061034f61040e565b151561035a57600080fd5b83610364816107e4565b1561036e57600080fd5b61037b8585856000610805565b95945050505050565b61038c61040e565b151561039757600080fd5b60038054829081106103a557fe5b90600052602060002090600a02016008015442101580156103f0575060038054829081106103cf57fe5b90600052602060002090600a020160060160019054906101000a900460ff16155b15156103fb57600080fd5b610404816109c4565b50565b6001545b90565b6000610419336107e4565b905090565b60035490565b60025481565b600081151561043857600080fd5b50600160a060020a03919091166000908152602081905260409020600301541490565b600061046561040e565b151561047057600080fd5b50600160a060020a03331660009081526020819052604090206003015490565b600061049a61040e565b15156104a557600080fd5b826104af816107e4565b15156104ba57600080fd5b600160a060020a0384166000908152602081905260409020600301546104e4908590856001610805565b949350505050565b600080600080600060038681548110151561050357fe5b600091825260209091206006600a9092020101546003805460ff9092169650908790811061052d57fe5b90600052602060002090600a020160060160019054906101000a900460ff16935060038681548110151561055d57fe5b60009182526020909120600a909102015460038054600160a060020a039092169450908790811061058a57fe5b90600052602060002090600a02016001015491506003868154811015156105ad57fe5b90600052602060002090600a020160020160009054906101000a900460ff16905091939590929450565b6105df61040e565b15156105ea57600080fd5b60038054839081106105f857fe5b60009182526020808320600160a060020a03331684526003600a90930201919091019052604090205460ff161580156106765750600380548390811061063a57fe5b90600052602060002090600a02016009015460008033600160a060020a0316600160a060020a0316815260200190815260200160002060010154105b151561068157600080fd5b600380548390811061068f57fe5b90600052602060002090600a02016008015442101561079d5760016003838154811015156106b957fe5b6000918252602080832033600160a060020a03168452600a92909202909101600301905260409020805460ff1916911515919091179055801561072357600380548390811061070457fe5b600091825260209091206004600a90920201018054600101905561074c565b600380548390811061073157fe5b600091825260209091206005600a9092020101805460010190555b7f030b0f8dcd86a031eddb071f91882edeac8173663ba775713b677b42b51be44b8233604051918252600160a060020a031660208201526040908101905180910390a161079882610bb6565b6107a6565b6107a6826109c4565b5050565b60018054829081106107b857fe5b600091825260209091200154600160a060020a0316905081565b60006107de338361042a565b92915050565b600160a060020a031660009081526020819052604090206002015460ff1690565b60008060006108126113d7565b600160a060020a038816815260208101879052429250610e1086028301915060006040820186600181111561084357fe5b9081600181111561085057fe5b9052506001805460e084015261010083018490526101208301859052600380549091810161087e8382611430565b600092835260209092208491600a02018151815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039190911617815560208201516001820155604082015160028201805460ff1916600183818111156108de57fe5b0217905550606082015181600401556080820151816005015560a082015160068201805460ff191691151591909117905560c08201516006820180549115156101000261ff001990921691909117905560e0820151816007015561010082015181600801556101208201518160090155505090506001810394507f35f026da253c11f9f1eff4a76b197ab9170827af5f0e8a05d6f97bbf665b52f2858a85338b604051948552600160a060020a03938416602086015260408086019390935292166060840152608083019190915260a0909101905180910390a150505050949350505050565b6000806109d083610f25565b91506109db83610f53565b90506109e78183610f9f565b15610a925760038054849081106109fa57fe5b90600052602060002090600a020160050154600384815481101515610a1b57fe5b90600052602060002090600a02016004015411600384815481101515610a3d57fe5b60009182526020909120600a90910201600601805460ff19169115159190911790556003805484908110610a6d57fe5b600091825260209091206006600a90920201015460ff1615610a9257610a9283610faa565b6001600384815481101515610aa357fe5b90600052602060002090600a020160060160016101000a81548160ff0219169083151502179055507fbf15f4f63d7b8fb95beb6a5cf1db6cb9222eac9908f647ae41e0ec5a7624bd9783600385815481101515610afc57fe5b600091825260209091206006600a9092020101546003805460ff9092169187908110610b2457fe5b90600052602060002090600a020160040154600387815481101515610b4557fe5b90600052602060002090600a02016005015485600389815481101515610b6757fe5b90600052602060002090600a02016007015460405195865293151560208601526040808601939093526060850191909152608084015260a083019190915260c0909101905180910390a1505050565b6000806000610bc484610f25565b9250610bcf84610f53565b9150610bdb8284610f9f565b15610f1f5781600385815481101515610bf057fe5b90600052602060002090600a020160070154039050600384815481101515610c1457fe5b90600052602060002090600a0201600401548101600385815481101515610c3757fe5b90600052602060002090600a020160050154101515610d74576001600385815481101515610c6157fe5b90600052602060002090600a020160060160016101000a81548160ff0219169083151502179055507fbf15f4f63d7b8fb95beb6a5cf1db6cb9222eac9908f647ae41e0ec5a7624bd9784600386815481101515610cba57fe5b600091825260209091206006600a9092020101546003805460ff9092169188908110610ce257fe5b90600052602060002090600a020160040154600388815481101515610d0357fe5b90600052602060002090600a0201600501548660038a815481101515610d2557fe5b90600052602060002090600a02016007015460405195865293151560208601526040808601939093526060850191909152608084015260a083019190915260c0909101905180910390a1610f1f565b6003805485908110610d8257fe5b90600052602060002090600a0201600501548101600385815481101515610da557fe5b90600052602060002090600a0201600401541115610f1f57610dc684610faa565b6001600385815481101515610dd757fe5b90600052602060002090600a020160060160006101000a81548160ff0219169083151502179055506001600385815481101515610e1057fe5b90600052602060002090600a020160060160016101000a81548160ff0219169083151502179055507fbf15f4f63d7b8fb95beb6a5cf1db6cb9222eac9908f647ae41e0ec5a7624bd9784600386815481101515610e6957fe5b600091825260209091206006600a9092020101546003805460ff9092169188908110610e9157fe5b90600052602060002090600a020160040154600388815481101515610eb257fe5b90600052602060002090600a0201600501548660038a815481101515610ed457fe5b90600052602060002090600a02016007015460405195865293151560208601526040808601939093526060850191909152608084015260a083019190915260c0909101905180910390a15b50505050565b6000600382815481101515610f3657fe5b90600052602060002090600a020160070154600254029050919050565b6000600382815481101515610f6457fe5b90600052602060002090600a020160050154600383815481101515610f8557fe5b90600052602060002090600a020160040154019050919050565b606491909102101590565b60006003805483908110610fba57fe5b600091825260209091206002600a90920201015460ff166001811115610fdc57fe5b141561103b57611036600382815481101515610ff457fe5b60009182526020909120600a909102015460038054600160a060020a03909216918490811061101f57fe5b90600052602060002090600a0201600101546110c7565b610404565b6001600380548390811061104b57fe5b600091825260209091206002600a90920201015460ff16600181111561106d57fe5b14156104045761040460038281548110151561108557fe5b60009182526020909120600a909102015460038054600160a060020a0390921691849081106110b057fe5b90600052602060002090600a0201600101546111e4565b6110d0826107e4565b1580156110e457506110e2828261042a565b155b15156110ef57600080fd5b600180548082016111008382611461565b506000918252602080832091909101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0386169081179091556001805491845291839052604092839020600019909101815560028101805460ff19168317905542918101919091556003018290557f144590f0e8efadd724bf6109eaef479b6501fce4a7f9601fac3a6e702dad900c90839051600160a060020a0390911681526040602082018190526005818301527f616464656400000000000000000000000000000000000000000000000000000060608301526080909101905180910390a15050565b6000806111f0846107e4565b80156112015750611201848461042a565b151561120c57600080fd5b5050600160a060020a038216600090815260208190526040902054805b600154600019018110156112dc576001805482820190811061124757fe5b60009182526020909120015460018054600160a060020a03909216918390811061126d57fe5b906000526020600020900160006101000a815481600160a060020a030219169083600160a060020a03160217905550806000806001848154811015156112af57fe5b6000918252602080832090910154600160a060020a03168352820192909252604001902055600101611229565b6001805460001981019081106112ee57fe5b6000918252602090912001805473ffffffffffffffffffffffffffffffffffffffff191690556001805490611327906000198301611461565b50600160a060020a0384166000908152602081905260408082208281556001810183905560028101805460ff19169055600301919091557f144590f0e8efadd724bf6109eaef479b6501fce4a7f9601fac3a6e702dad900c90859051600160a060020a0390911681526040602082018190526007818301527f72656d6f7665640000000000000000000000000000000000000000000000000060608301526080909101905180910390a150505050565b610140604051908101604090815260008083526020830181905290820190815260200160008152602001600081526020016000151581526020016000151581526020016000815260200160008152602001600081525090565b81548183558181151161145c57600a0281600a02836000526020600020918201910161145c9190611485565b505050565b81548183558181151161145c5760008381526020902061145c9181019083016114fd565b61040b91905b808211156114f957805473ffffffffffffffffffffffffffffffffffffffff1916815560006001820181905560028201805460ff19169055600482018190556005820181905560068201805461ffff1916905560078201819055600882018190556009820155600a0161148b565b5090565b61040b91905b808211156114f957600081556001016115035600a165627a7a723058208b274a789036ddc193d917bce881376442b8a0ef5adce70f00156bc54b12e4430029";

    protected ConsortiumContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ConsortiumContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<MembershipChangedEventResponse> getMembershipChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MembershipChanged", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<MembershipChangedEventResponse> responses = new ArrayList<MembershipChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MembershipChangedEventResponse typedResponse = new MembershipChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.member = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MembershipChangedEventResponse> membershipChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MembershipChanged", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MembershipChangedEventResponse>() {
            @Override
            public MembershipChangedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                MembershipChangedEventResponse typedResponse = new MembershipChangedEventResponse();
                typedResponse.log = log;
                typedResponse.member = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<ProposalAddedEventResponse> getProposalAddedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ProposalAdded", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ProposalAddedEventResponse> responses = new ArrayList<ProposalAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProposalAddedEventResponse typedResponse = new ProposalAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.member = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.votingDeadline = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.creator = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.duration = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ProposalAddedEventResponse> proposalAddedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ProposalAdded", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ProposalAddedEventResponse>() {
            @Override
            public ProposalAddedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ProposalAddedEventResponse typedResponse = new ProposalAddedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.member = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.votingDeadline = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.creator = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.duration = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public List<VotedEventResponse> getVotedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Voted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<VotedEventResponse> responses = new ArrayList<VotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotedEventResponse typedResponse = new VotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.voter = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<VotedEventResponse> votedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Voted", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, VotedEventResponse>() {
            @Override
            public VotedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                VotedEventResponse typedResponse = new VotedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.voter = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<ProposalTalliedEventResponse> getProposalTalliedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ProposalTallied", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ProposalTalliedEventResponse> responses = new ArrayList<ProposalTalliedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProposalTalliedEventResponse typedResponse = new ProposalTalliedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.result = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.votesFor = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.votesAgainst = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.nbOfVoters = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.maxVoters = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ProposalTalliedEventResponse> proposalTalliedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ProposalTallied", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ProposalTalliedEventResponse>() {
            @Override
            public ProposalTalliedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ProposalTalliedEventResponse typedResponse = new ProposalTalliedEventResponse();
                typedResponse.log = log;
                typedResponse.proposalID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.result = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.votesFor = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.votesAgainst = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.nbOfVoters = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.maxVoters = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<Tuple4<BigInteger, BigInteger, Boolean, byte[]>> members(String param0) {
        final Function function = new Function("members", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple4<BigInteger, BigInteger, Boolean, byte[]>>(
                new Callable<Tuple4<BigInteger, BigInteger, Boolean, byte[]>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, Boolean, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, Boolean, byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> requestAddMember(String _targetMember, byte[] _tslIdentifier, BigInteger _duration) {
        final Function function = new Function(
                "requestAddMember", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_targetMember), 
                new org.web3j.abi.datatypes.generated.Bytes32(_tslIdentifier), 
                new org.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> executeProposal(BigInteger _proposalID) {
        final Function function = new Function(
                "executeProposal", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_proposalID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> countMembers() {
        final Function function = new Function("countMembers", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isAuthorized() {
        final Function function = new Function("isAuthorized", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> countProposals() {
        final Function function = new Function("countProposals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> quorumInPercent() {
        final Function function = new Function("quorumInPercent", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isSignerTsl(String _member, byte[] _tslIdentifier) {
        final Function function = new Function("isSignerTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_member), 
                new org.web3j.abi.datatypes.generated.Bytes32(_tslIdentifier)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<byte[]> authorizedTsl() {
        final Function function = new Function("authorizedTsl", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> requestRemoveMember(String _targetMember, BigInteger _duration) {
        final Function function = new Function(
                "requestRemoveMember", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_targetMember), 
                new org.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple5<Boolean, Boolean, String, byte[], BigInteger>> getProposal(BigInteger index) {
        final Function function = new Function("getProposal", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple5<Boolean, Boolean, String, byte[], BigInteger>>(
                new Callable<Tuple5<Boolean, Boolean, String, byte[], BigInteger>>() {
                    @Override
                    public Tuple5<Boolean, Boolean, String, byte[], BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<Boolean, Boolean, String, byte[], BigInteger>(
                                (Boolean) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> vote(BigInteger _proposalID, Boolean _inSupport) {
        final Function function = new Function(
                "vote", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_proposalID), 
                new org.web3j.abi.datatypes.Bool(_inSupport)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> memberAddresses(BigInteger param0) {
        final Function function = new Function("memberAddresses", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isSignerTsl(byte[] _tslIdentifier) {
        final Function function = new Function("isSignerTsl", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_tslIdentifier)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> isAuthorized(String _member) {
        final Function function = new Function("isAuthorized", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_member)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<ConsortiumContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _quorumInPercent, byte[] _tslIdentifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_quorumInPercent), 
                new org.web3j.abi.datatypes.generated.Bytes32(_tslIdentifier)));
        return deployRemoteCall(ConsortiumContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ConsortiumContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _quorumInPercent, byte[] _tslIdentifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_quorumInPercent), 
                new org.web3j.abi.datatypes.generated.Bytes32(_tslIdentifier)));
        return deployRemoteCall(ConsortiumContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static ConsortiumContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConsortiumContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ConsortiumContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConsortiumContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class MembershipChangedEventResponse {
        public Log log;

        public String member;

        public String description;
    }

    public static class ProposalAddedEventResponse {
        public Log log;

        public BigInteger proposalID;

        public String member;

        public BigInteger votingDeadline;

        public String creator;

        public BigInteger duration;
    }

    public static class VotedEventResponse {
        public Log log;

        public BigInteger proposalID;

        public String voter;
    }

    public static class ProposalTalliedEventResponse {
        public Log log;

        public BigInteger proposalID;

        public Boolean result;

        public BigInteger votesFor;

        public BigInteger votesAgainst;

        public BigInteger nbOfVoters;

        public BigInteger maxVoters;
    }
}
