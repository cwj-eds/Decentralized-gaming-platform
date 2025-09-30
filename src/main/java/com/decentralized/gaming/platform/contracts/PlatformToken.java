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
import org.web3j.abi.datatypes.generated.Uint8;
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
public class PlatformToken extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f5ffd5b50604051610f16380380610f1683398101604081905261002e916102fa565b338282600361003d83826103e3565b50600461004a82826103e3565b5050506001600160a01b03811661007b57604051631e4fbdf760e01b81525f60048201526024015b60405180910390fd5b610084816100ae565b506100a7336100956012600a610596565b6100a290620f42406105ab565b6100ff565b50506105d5565b600580546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b0382166101285760405163ec442f0560e01b81525f6004820152602401610072565b6101335f8383610137565b5050565b6001600160a01b038316610161578060025f82825461015691906105c2565b909155506101d19050565b6001600160a01b0383165f90815260208190526040902054818110156101b35760405163391434e360e21b81526001600160a01b03851660048201526024810182905260448101839052606401610072565b6001600160a01b0384165f9081526020819052604090209082900390555b6001600160a01b0382166101ed5760028054829003905561020b565b6001600160a01b0382165f9081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8360405161025091815260200190565b60405180910390a3505050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610280575f5ffd5b81516001600160401b038111156102995761029961025d565b604051601f8201601f19908116603f011681016001600160401b03811182821017156102c7576102c761025d565b6040528181528382016020018510156102de575f5ffd5b8160208501602083015e5f918101602001919091529392505050565b5f5f6040838503121561030b575f5ffd5b82516001600160401b03811115610320575f5ffd5b61032c85828601610271565b602085015190935090506001600160401b03811115610349575f5ffd5b61035585828601610271565b9150509250929050565b600181811c9082168061037357607f821691505b60208210810361039157634e487b7160e01b5f52602260045260245ffd5b50919050565b601f8211156103de57805f5260205f20601f840160051c810160208510156103bc5750805b601f840160051c820191505b818110156103db575f81556001016103c8565b50505b505050565b81516001600160401b038111156103fc576103fc61025d565b6104108161040a845461035f565b84610397565b6020601f821160018114610442575f831561042b5750848201515b5f19600385901b1c1916600184901b1784556103db565b5f84815260208120601f198516915b828110156104715787850151825560209485019460019092019101610451565b508482101561048e57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b634e487b7160e01b5f52601160045260245ffd5b6001815b60018411156104ec578085048111156104d0576104d061049d565b60018416156104de57908102905b60019390931c9280026104b5565b935093915050565b5f8261050257506001610590565b8161050e57505f610590565b8160018114610524576002811461052e5761054a565b6001915050610590565b60ff84111561053f5761053f61049d565b50506001821b610590565b5060208310610133831016604e8410600b841016171561056d575081810a610590565b6105795f1984846104b1565b805f190482111561058c5761058c61049d565b0290505b92915050565b5f6105a460ff8416836104f4565b9392505050565b80820281158282048414176105905761059061049d565b808201808211156105905761059061049d565b610934806105e25f395ff3fe608060405234801561000f575f5ffd5b50600436106100f0575f3560e01c806342966c681161009357806395d89b411161006357806395d89b41146101ef578063a9059cbb146101f7578063dd62ed3e1461020a578063f2fde38b14610242575f5ffd5b806342966c681461019157806370a08231146101a4578063715018a6146101cc5780638da5cb5b146101d4575f5ffd5b806318160ddd116100ce57806318160ddd1461014a57806323b872dd1461015c578063313ce5671461016f57806340c10f191461017e575f5ffd5b806306dd0419146100f457806306fdde0314610109578063095ea7b314610127575b5f5ffd5b6101076101023660046107a8565b610255565b005b61011161026b565b60405161011e91906107d0565b60405180910390f35b61013a6101353660046107a8565b6102fb565b604051901515815260200161011e565b6002545b60405190815260200161011e565b61013a61016a366004610805565b610314565b6040516012815260200161011e565b61010761018c3660046107a8565b610337565b61010761019f36600461083f565b610349565b61014e6101b2366004610856565b6001600160a01b03165f9081526020819052604090205490565b610107610356565b6005546040516001600160a01b03909116815260200161011e565b610111610369565b61013a6102053660046107a8565b610378565b61014e610218366004610876565b6001600160a01b039182165f90815260016020908152604080832093909416825291909152205490565b610107610250366004610856565b610385565b61025d6103c4565b61026782826103f1565b5050565b60606003805461027a906108a7565b80601f01602080910402602001604051908101604052809291908181526020018280546102a6906108a7565b80156102f15780601f106102c8576101008083540402835291602001916102f1565b820191905f5260205f20905b8154815290600101906020018083116102d457829003601f168201915b5050505050905090565b5f33610308818585610425565b60019150505b92915050565b5f33610321858285610437565b61032c8585856104b3565b506001949350505050565b61033f6103c4565b6102678282610510565b61035333826103f1565b50565b61035e6103c4565b6103675f610544565b565b60606004805461027a906108a7565b5f336103088185856104b3565b61038d6103c4565b6001600160a01b0381166103bb57604051631e4fbdf760e01b81525f60048201526024015b60405180910390fd5b61035381610544565b6005546001600160a01b031633146103675760405163118cdaa760e01b81523360048201526024016103b2565b6001600160a01b03821661041a57604051634b637e8f60e11b81525f60048201526024016103b2565b610267825f83610595565b61043283838360016106bb565b505050565b6001600160a01b038381165f908152600160209081526040808320938616835292905220545f198110156104ad578181101561049f57604051637dc7a0d960e11b81526001600160a01b038416600482015260248101829052604481018390526064016103b2565b6104ad84848484035f6106bb565b50505050565b6001600160a01b0383166104dc57604051634b637e8f60e11b81525f60048201526024016103b2565b6001600160a01b0382166105055760405163ec442f0560e01b81525f60048201526024016103b2565b610432838383610595565b6001600160a01b0382166105395760405163ec442f0560e01b81525f60048201526024016103b2565b6102675f8383610595565b600580546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b0383166105bf578060025f8282546105b491906108df565b9091555061062f9050565b6001600160a01b0383165f90815260208190526040902054818110156106115760405163391434e360e21b81526001600160a01b038516600482015260248101829052604481018390526064016103b2565b6001600160a01b0384165f9081526020819052604090209082900390555b6001600160a01b03821661064b57600280548290039055610669565b6001600160a01b0382165f9081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516106ae91815260200190565b60405180910390a3505050565b6001600160a01b0384166106e45760405163e602df0560e01b81525f60048201526024016103b2565b6001600160a01b03831661070d57604051634a1406b160e11b81525f60048201526024016103b2565b6001600160a01b038085165f90815260016020908152604080832093871683529290522082905580156104ad57826001600160a01b0316846001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9258460405161077f91815260200190565b60405180910390a350505050565b80356001600160a01b03811681146107a3575f5ffd5b919050565b5f5f604083850312156107b9575f5ffd5b6107c28361078d565b946020939093013593505050565b602081525f82518060208401528060208501604085015e5f604082850101526040601f19601f83011684010191505092915050565b5f5f5f60608486031215610817575f5ffd5b6108208461078d565b925061082e6020850161078d565b929592945050506040919091013590565b5f6020828403121561084f575f5ffd5b5035919050565b5f60208284031215610866575f5ffd5b61086f8261078d565b9392505050565b5f5f60408385031215610887575f5ffd5b6108908361078d565b915061089e6020840161078d565b90509250929050565b600181811c908216806108bb57607f821691505b6020821081036108d957634e487b7160e01b5f52602260045260245ffd5b50919050565b8082018082111561030e57634e487b7160e01b5f52601160045260245ffdfea2646970667358221220d65648d642f4cdda815c08264ac8763d6968101b0411a818a298ff782a9ae49e64736f6c634300081c0033";

    public static final String FUNC_ADMINBURN = "adminBurn";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected PlatformToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PlatformToken(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PlatformToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PlatformToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
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

    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> adminBurn(String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_ADMINBURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.Address(160, spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> burn(BigInteger amount) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(String to, BigInteger amount) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static PlatformToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PlatformToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PlatformToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PlatformToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PlatformToken load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PlatformToken(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PlatformToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PlatformToken(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PlatformToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String name_, String symbol_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_)));
        return deployRemoteCall(PlatformToken.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PlatformToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name_, String symbol_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_)));
        return deployRemoteCall(PlatformToken.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PlatformToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_)));
        return deployRemoteCall(PlatformToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PlatformToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_)));
        return deployRemoteCall(PlatformToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
