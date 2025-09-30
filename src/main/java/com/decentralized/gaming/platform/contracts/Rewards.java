package com.decentralized.gaming.platform.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.3.
 */
@SuppressWarnings("rawtypes")
public class Rewards extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f5ffd5b5060405161073638038061073683398101604081905261002e916100d1565b338061005357604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b61005c81610082565b50600180546001600160a01b0319166001600160a01b03929092169190911790556100fe565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f602082840312156100e1575f5ffd5b81516001600160a01b03811681146100f7575f5ffd5b9392505050565b61062b8061010b5f395ff3fe608060405234801561000f575f5ffd5b5060043610610060575f3560e01c806303e7d393146100645780631ba9b0a514610079578063715018a61461008c5780638da5cb5b14610094578063f2fde38b146100bc578063fc0c546a146100cf575b5f5ffd5b610077610072366004610456565b6100e2565b005b6100776100873660046104ed565b610196565b61007761032b565b5f546001600160a01b03165b6040516001600160a01b03909116815260200160405180910390f35b6100776100ca36600461058c565b61033e565b6001546100a0906001600160a01b031681565b6100ea61037b565b6001546040516340c10f1960e01b81526001600160a01b03868116600483015260248201869052909116906340c10f19906044015f604051808303815f87803b158015610135575f5ffd5b505af1158015610147573d5f5f3e3d5ffd5b50505050836001600160a01b03167fac5e3ed0ccb5aba6bb73488fb597b4196fd1afe9c893ddeaad25dd71b31ed53c848484604051610188939291906105ac565b60405180910390a250505050565b61019e61037b565b8483146101e15760405162461bcd60e51b815260206004820152600c60248201526b0d8cadc40dad2e6dac2e8c6d60a31b60448201526064015b60405180910390fd5b5f5b85811015610322576001546001600160a01b03166340c10f1988888481811061020e5761020e6105e1565b9050602002016020810190610223919061058c565b878785818110610235576102356105e1565b6040516001600160e01b031960e087901b1681526001600160a01b03909416600485015260200291909101356024830152506044015f604051808303815f87803b158015610281575f5ffd5b505af1158015610293573d5f5f3e3d5ffd5b505050508686828181106102a9576102a96105e1565b90506020020160208101906102be919061058c565b6001600160a01b03167fac5e3ed0ccb5aba6bb73488fb597b4196fd1afe9c893ddeaad25dd71b31ed53c8686848181106102fa576102fa6105e1565b905060200201358585604051610312939291906105ac565b60405180910390a26001016101e3565b50505050505050565b61033361037b565b61033c5f6103a7565b565b61034661037b565b6001600160a01b03811661036f57604051631e4fbdf760e01b81525f60048201526024016101d8565b610378816103a7565b50565b5f546001600160a01b0316331461033c5760405163118cdaa760e01b81523360048201526024016101d8565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80356001600160a01b038116811461040c575f5ffd5b919050565b5f5f83601f840112610421575f5ffd5b50813567ffffffffffffffff811115610438575f5ffd5b60208301915083602082850101111561044f575f5ffd5b9250929050565b5f5f5f5f60608587031215610469575f5ffd5b610472856103f6565b935060208501359250604085013567ffffffffffffffff811115610494575f5ffd5b6104a087828801610411565b95989497509550505050565b5f5f83601f8401126104bc575f5ffd5b50813567ffffffffffffffff8111156104d3575f5ffd5b6020830191508360208260051b850101111561044f575f5ffd5b5f5f5f5f5f5f60608789031215610502575f5ffd5b863567ffffffffffffffff811115610518575f5ffd5b61052489828a016104ac565b909750955050602087013567ffffffffffffffff811115610543575f5ffd5b61054f89828a016104ac565b909550935050604087013567ffffffffffffffff81111561056e575f5ffd5b61057a89828a01610411565b979a9699509497509295939492505050565b5f6020828403121561059c575f5ffd5b6105a5826103f6565b9392505050565b83815260406020820152816040820152818360608301375f818301606090810191909152601f909201601f1916010192915050565b634e487b7160e01b5f52603260045260245ffdfea264697066735822122007705211bff8effd950e674a71fac4c730695dcf1a6d6368c49002520d14ff5c64736f6c634300081c0033";

    public static final String FUNC_BATCHISSUE = "batchIssue";

    public static final String FUNC_ISSUEREWARD = "issueReward";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_TOKEN = "token";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event REWARDISSUED_EVENT = new Event("RewardIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected Rewards(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Rewards(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Rewards(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Rewards(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<RewardIssuedEventResponse> getRewardIssuedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REWARDISSUED_EVENT, transactionReceipt);
        ArrayList<RewardIssuedEventResponse> responses = new ArrayList<RewardIssuedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RewardIssuedEventResponse typedResponse = new RewardIssuedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RewardIssuedEventResponse getRewardIssuedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(REWARDISSUED_EVENT, log);
        RewardIssuedEventResponse typedResponse = new RewardIssuedEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<RewardIssuedEventResponse> rewardIssuedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRewardIssuedEventFromLog(log));
    }

    public Flowable<RewardIssuedEventResponse> rewardIssuedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REWARDISSUED_EVENT));
        return rewardIssuedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> batchIssue(List<String> recipients, List<BigInteger> amounts, String reason) {
        final Function function = new Function(
                FUNC_BATCHISSUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(amounts, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.Utf8String(reason)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> issueReward(String to, BigInteger amount, String reason) {
        final Function function = new Function(
                FUNC_ISSUEREWARD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Utf8String(reason)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> token() {
        final Function function = new Function(FUNC_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Rewards load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Rewards(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Rewards load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Rewards(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Rewards load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Rewards(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Rewards load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Rewards(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Rewards> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _token)));
        return deployRemoteCall(Rewards.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Rewards> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _token)));
        return deployRemoteCall(Rewards.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Rewards> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _token)));
        return deployRemoteCall(Rewards.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Rewards> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _token)));
        return deployRemoteCall(Rewards.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class RewardIssuedEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger amount;

        public String reason;
    }
}
