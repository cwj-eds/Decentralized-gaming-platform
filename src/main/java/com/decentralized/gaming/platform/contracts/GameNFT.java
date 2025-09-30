package com.decentralized.gaming.platform.contracts;

import io.reactivex.Flowable;
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
import org.web3j.tuples.generated.Tuple5;
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
public class GameNFT extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f5ffd5b50604051611efc380380611efc83398101604081905261002e916101c7565b3385855f61003c83826102d9565b50600161004982826102d9565b5050506001600160a01b03811661007957604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b610082816100be565b50600980546001600160a01b039485166001600160a01b031991821617909155600a805493909416921691909117909155600b55506103939050565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610132575f5ffd5b81516001600160401b0381111561014b5761014b61010f565b604051601f8201601f19908116603f011681016001600160401b03811182821017156101795761017961010f565b604052818152838201602001851015610190575f5ffd5b8160208501602083015e5f918101602001919091529392505050565b80516001600160a01b03811681146101c2575f5ffd5b919050565b5f5f5f5f5f60a086880312156101db575f5ffd5b85516001600160401b038111156101f0575f5ffd5b6101fc88828901610123565b602088015190965090506001600160401b03811115610219575f5ffd5b61022588828901610123565b945050610234604087016101ac565b9250610242606087016101ac565b9150608086015190509295509295909350565b600181811c9082168061026957607f821691505b60208210810361028757634e487b7160e01b5f52602260045260245ffd5b50919050565b601f8211156102d457805f5260205f20601f840160051c810160208510156102b25750805b601f840160051c820191505b818110156102d1575f81556001016102be565b50505b505050565b81516001600160401b038111156102f2576102f261010f565b610306816103008454610255565b8461028d565b6020601f821160018114610338575f83156103215750848201515b5f19600385901b1c1916600184901b1784556102d1565b5f84815260208120601f198516915b828110156103675787850151825560209485019460019092019101610347565b508482101561038457868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b611b5c806103a05f395ff3fe608060405234801561000f575f5ffd5b506004361061016d575f3560e01c8063715018a6116100d9578063c87b56dd11610093578063dd87e1391161006e578063dd87e1391461032e578063e74b981b14610341578063e985e9c514610354578063f2fde38b14610367575f5ffd5b8063c87b56dd146102ff578063d1b812cd14610312578063dce0b4e414610325575f5ffd5b8063715018a6146102a55780638da5cb5b146102ad57806395d89b41146102be578063a22cb465146102c6578063b7d86225146102d9578063b88d4fde146102ec575f5ffd5b80631c7bd4cd1161012a5780631c7bd4cd1461022557806323b872dd1461024657806342842e0e14610259578063469048401461026c5780636352211e1461027f57806370a0823114610292575f5ffd5b806301ffc9a7146101715780630255a03b1461019957806306fdde03146101ae578063081812fc146101c3578063095ea7b3146101ee578063117a5b9014610201575b5f5ffd5b61018461017f3660046114d3565b61037a565b60405190151581526020015b60405180910390f35b6101ac6101a7366004611509565b6103a4565b005b6101b66103ce565b6040516101909190611550565b6101d66101d1366004611562565b61045d565b6040516001600160a01b039091168152602001610190565b6101ac6101fc366004611579565b610484565b61021461020f366004611562565b610493565b6040516101909594939291906115a1565b61023861023336600461169e565b61065c565b604051908152602001610190565b6101ac610254366004611753565b610894565b6101ac610267366004611753565b61091d565b600a546101d6906001600160a01b031681565b6101d661028d366004611562565b61093c565b6102386102a0366004611509565b610946565b6101ac61098b565b6007546001600160a01b03166101d6565b6101b661099e565b6101ac6102d436600461179a565b6109ad565b6101ac6102e7366004611562565b6109b8565b6101ac6102fa3660046117cf565b6109c5565b6101b661030d366004611562565b6109dd565b6009546101d6906001600160a01b031681565b610238600b5481565b61023861033c366004611839565b610ae8565b6101ac61034f366004611509565b610c10565b6101846103623660046118fe565b610c3a565b6101ac610375366004611509565b610c67565b5f6001600160e01b03198216632483248360e11b148061039e575061039e82610ca4565b92915050565b6103ac610cf3565b600980546001600160a01b0319166001600160a01b0392909216919091179055565b60605f80546103dc9061192f565b80601f01602080910402602001604051908101604052809291908181526020018280546104089061192f565b80156104535780601f1061042a57610100808354040283529160200191610453565b820191905f5260205f20905b81548152906001019060200180831161043657829003601f168201915b5050505050905090565b5f61046782610d20565b505f828152600460205260409020546001600160a01b031661039e565b61048f828233610d58565b5050565b600c6020525f90815260409020805481906104ad9061192f565b80601f01602080910402602001604051908101604052809291908181526020018280546104d99061192f565b80156105245780601f106104fb57610100808354040283529160200191610524565b820191905f5260205f20905b81548152906001019060200180831161050757829003601f168201915b5050505050908060010180546105399061192f565b80601f01602080910402602001604051908101604052809291908181526020018280546105659061192f565b80156105b05780601f10610587576101008083540402835291602001916105b0565b820191905f5260205f20905b81548152906001019060200180831161059357829003601f168201915b5050505050908060020180546105c59061192f565b80601f01602080910402602001604051908101604052809291908181526020018280546105f19061192f565b801561063c5780601f106106135761010080835404028352916020019161063c565b820191905f5260205f20905b81548152906001019060200180831161061f57829003601f168201915b50505050600383015460049093015491926001600160a01b031691905085565b600b545f901561077e576009546001600160a01b03166106bc5760405162461bcd60e51b8152602060048201526016602482015275141b185d199bdc9b481d1bdad95b881b9bdd081cd95d60521b60448201526064015b60405180910390fd5b600954600a54600b546040516323b872dd60e01b81523360048201526001600160a01b03928316602482015260448101919091529116906323b872dd906064016020604051808303815f875af1158015610718573d5f5f3e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061073c9190611967565b61077e5760405162461bcd60e51b8152602060048201526013602482015272119959481d1c985b9cd9995c8819985a5b1959606a1b60448201526064016106b3565b60088054905f61078d83611982565b909155505060085461079f3382610d65565b8251156107b0576107b08184610dc6565b6040805160a08101825287815260208082018890528183018790523360608301524260808301525f848152600c90915291909120815181906107f290826119ea565b506020820151600182019061080790826119ea565b506040820151600282019061081c90826119ea565b5060608201516003820180546001600160a01b0319166001600160a01b03909216919091179055608090910151600490910155604051339082907fb8c10eb2cbd5882e65e2a0353d66716331f119893ba9066154c6500c4a66952590610883908890611550565b60405180910390a395945050505050565b6001600160a01b0382166108bd57604051633250574960e11b81525f60048201526024016106b3565b5f6108c9838333610e15565b9050836001600160a01b0316816001600160a01b031614610917576040516364283d7b60e01b81526001600160a01b03808616600483015260248201849052821660448201526064016106b3565b50505050565b61093783838360405180602001604052805f8152506109c5565b505050565b5f61039e82610d20565b5f6001600160a01b038216610970576040516322718ad960e21b81525f60048201526024016106b3565b506001600160a01b03165f9081526003602052604090205490565b610993610cf3565b61099c5f610f07565b565b6060600180546103dc9061192f565b61048f338383610f58565b6109c0610cf3565b600b55565b6109d0848484610894565b6109173385858585610ff6565b60606109e882610d20565b505f8281526006602052604081208054610a019061192f565b80601f0160208091040260200160405190810160405280929190818152602001828054610a2d9061192f565b8015610a785780601f10610a4f57610100808354040283529160200191610a78565b820191905f5260205f20905b815481529060010190602001808311610a5b57829003601f168201915b505050505090505f610a9460408051602081019091525f815290565b905080515f03610aa5575092915050565b815115610ad7578082604051602001610abf929190611abb565b60405160208183030381529060405292505050919050565b610ae08461111e565b949350505050565b5f610af1610cf3565b60088054905f610b0083611982565b9091555050600854610b128782610d65565b825115610b2357610b238184610dc6565b6040805160a08101825287815260208082018890528183018790526001600160a01b038a1660608301524260808301525f848152600c9091529190912081518190610b6e90826119ea565b5060208201516001820190610b8390826119ea565b5060408201516002820190610b9890826119ea565b5060608201516003820180546001600160a01b0319166001600160a01b039283161790556080909201516004909101556040519088169082907fb8c10eb2cbd5882e65e2a0353d66716331f119893ba9066154c6500c4a66952590610bfe908890611550565b60405180910390a39695505050505050565b610c18610cf3565b600a80546001600160a01b0319166001600160a01b0392909216919091179055565b6001600160a01b039182165f90815260056020908152604080832093909416825291909152205460ff1690565b610c6f610cf3565b6001600160a01b038116610c9857604051631e4fbdf760e01b81525f60048201526024016106b3565b610ca181610f07565b50565b5f6001600160e01b031982166380ac58cd60e01b1480610cd457506001600160e01b03198216635b5e139f60e01b145b8061039e57506301ffc9a760e01b6001600160e01b031983161461039e565b6007546001600160a01b0316331461099c5760405163118cdaa760e01b81523360048201526024016106b3565b5f818152600260205260408120546001600160a01b03168061039e57604051637e27328960e01b8152600481018490526024016106b3565b610937838383600161118f565b6001600160a01b038216610d8e57604051633250574960e11b81525f60048201526024016106b3565b5f610d9a83835f610e15565b90506001600160a01b03811615610937576040516339e3563760e11b81525f60048201526024016106b3565b5f828152600660205260409020610ddd82826119ea565b506040518281527ff8e1a15aba9398e019f0b49df1a4fde98ee17ae345cb5f6b5e2c27f5033e8ce79060200160405180910390a15050565b5f828152600260205260408120546001600160a01b0390811690831615610e4157610e41818486611293565b6001600160a01b03811615610e7b57610e5c5f855f5f61118f565b6001600160a01b0381165f90815260036020526040902080545f190190555b6001600160a01b03851615610ea9576001600160a01b0385165f908152600360205260409020805460010190555b5f8481526002602052604080822080546001600160a01b0319166001600160a01b0389811691821790925591518793918516917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4949350505050565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b038216610f8a57604051630b61174360e31b81526001600160a01b03831660048201526024016106b3565b6001600160a01b038381165f81815260056020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b6001600160a01b0383163b1561111757604051630a85bd0160e11b81526001600160a01b0384169063150b7a0290611038908890889087908790600401611acf565b6020604051808303815f875af1925050508015611072575060408051601f3d908101601f1916820190925261106f91810190611b0b565b60015b6110d9573d80801561109f576040519150601f19603f3d011682016040523d82523d5f602084013e6110a4565b606091505b5080515f036110d157604051633250574960e11b81526001600160a01b03851660048201526024016106b3565b805160208201fd5b6001600160e01b03198116630a85bd0160e11b1461111557604051633250574960e11b81526001600160a01b03851660048201526024016106b3565b505b5050505050565b606061112982610d20565b505f61113f60408051602081019091525f815290565b90505f81511161115d5760405180602001604052805f815250611188565b80611167846112f7565b604051602001611178929190611abb565b6040516020818303038152906040525b9392505050565b80806111a357506001600160a01b03821615155b15611264575f6111b284610d20565b90506001600160a01b038316158015906111de5750826001600160a01b0316816001600160a01b031614155b80156111f157506111ef8184610c3a565b155b1561121a5760405163a9fbf51f60e01b81526001600160a01b03841660048201526024016106b3565b81156112625783856001600160a01b0316826001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45b505b50505f90815260046020526040902080546001600160a01b0319166001600160a01b0392909216919091179055565b61129e838383611386565b610937576001600160a01b0383166112cc57604051637e27328960e01b8152600481018290526024016106b3565b60405163177e802f60e01b81526001600160a01b0383166004820152602481018290526044016106b3565b60605f611303836113e7565b60010190505f816001600160401b03811115611321576113216115f7565b6040519080825280601f01601f19166020018201604052801561134b576020820181803683370190505b5090508181016020015b5f19016f181899199a1a9b1b9c1cb0b131b232b360811b600a86061a8153600a850494508461135557509392505050565b5f6001600160a01b03831615801590610ae05750826001600160a01b0316846001600160a01b031614806113bf57506113bf8484610c3a565b80610ae05750505f908152600460205260409020546001600160a01b03908116911614919050565b5f8072184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b83106114255772184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b830492506040015b6d04ee2d6d415b85acef81000000008310611451576d04ee2d6d415b85acef8100000000830492506020015b662386f26fc10000831061146f57662386f26fc10000830492506010015b6305f5e1008310611487576305f5e100830492506008015b612710831061149b57612710830492506004015b606483106114ad576064830492506002015b600a831061039e5760010192915050565b6001600160e01b031981168114610ca1575f5ffd5b5f602082840312156114e3575f5ffd5b8135611188816114be565b80356001600160a01b0381168114611504575f5ffd5b919050565b5f60208284031215611519575f5ffd5b611188826114ee565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b602081525f6111886020830184611522565b5f60208284031215611572575f5ffd5b5035919050565b5f5f6040838503121561158a575f5ffd5b611593836114ee565b946020939093013593505050565b60a081525f6115b360a0830188611522565b82810360208401526115c58188611522565b905082810360408401526115d98187611522565b6001600160a01b039590951660608401525050608001529392505050565b634e487b7160e01b5f52604160045260245ffd5b5f5f6001600160401b03841115611624576116246115f7565b50604051601f19601f85018116603f011681018181106001600160401b0382111715611652576116526115f7565b604052838152905080828401851015611669575f5ffd5b838360208301375f60208583010152509392505050565b5f82601f83011261168f575f5ffd5b6111888383356020850161160b565b5f5f5f5f608085870312156116b1575f5ffd5b84356001600160401b038111156116c6575f5ffd5b6116d287828801611680565b94505060208501356001600160401b038111156116ed575f5ffd5b6116f987828801611680565b93505060408501356001600160401b03811115611714575f5ffd5b61172087828801611680565b92505060608501356001600160401b0381111561173b575f5ffd5b61174787828801611680565b91505092959194509250565b5f5f5f60608486031215611765575f5ffd5b61176e846114ee565b925061177c602085016114ee565b929592945050506040919091013590565b8015158114610ca1575f5ffd5b5f5f604083850312156117ab575f5ffd5b6117b4836114ee565b915060208301356117c48161178d565b809150509250929050565b5f5f5f5f608085870312156117e2575f5ffd5b6117eb856114ee565b93506117f9602086016114ee565b92506040850135915060608501356001600160401b0381111561181a575f5ffd5b8501601f8101871361182a575f5ffd5b6117478782356020840161160b565b5f5f5f5f5f60a0868803121561184d575f5ffd5b611856866114ee565b945060208601356001600160401b03811115611870575f5ffd5b61187c88828901611680565b94505060408601356001600160401b03811115611897575f5ffd5b6118a388828901611680565b93505060608601356001600160401b038111156118be575f5ffd5b6118ca88828901611680565b92505060808601356001600160401b038111156118e5575f5ffd5b6118f188828901611680565b9150509295509295909350565b5f5f6040838503121561190f575f5ffd5b611918836114ee565b9150611926602084016114ee565b90509250929050565b600181811c9082168061194357607f821691505b60208210810361196157634e487b7160e01b5f52602260045260245ffd5b50919050565b5f60208284031215611977575f5ffd5b81516111888161178d565b5f6001820161199f57634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f82111561093757805f5260205f20601f840160051c810160208510156119cb5750805b601f840160051c820191505b81811015611117575f81556001016119d7565b81516001600160401b03811115611a0357611a036115f7565b611a1781611a11845461192f565b846119a6565b6020601f821160018114611a49575f8315611a325750848201515b5f19600385901b1c1916600184901b178455611117565b5f84815260208120601f198516915b82811015611a785787850151825560209485019460019092019101611a58565b5084821015611a9557868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b5f81518060208401855e5f93019283525090919050565b5f610ae0611ac98386611aa4565b84611aa4565b6001600160a01b03858116825284166020820152604081018390526080606082018190525f90611b0190830184611522565b9695505050505050565b5f60208284031215611b1b575f5ffd5b8151611188816114be56fea2646970667358221220d129c5946261efd1d7d5675e832a1a46da172f0ac84f476bfd28a47aaa51f28564736f6c634300081c0033";

    public static final String FUNC_ADMINMINT = "adminMint";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_CREATEGAME = "createGame";

    public static final String FUNC_CREATIONFEE = "creationFee";

    public static final String FUNC_FEERECIPIENT = "feeRecipient";

    public static final String FUNC_GAMES = "games";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_PLATFORMTOKEN = "platformToken";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_safeTransferFrom = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SETCREATIONFEE = "setCreationFee";

    public static final String FUNC_SETFEERECIPIENT = "setFeeRecipient";

    public static final String FUNC_SETPLATFORMTOKEN = "setPlatformToken";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event BATCHMETADATAUPDATE_EVENT = new Event("BatchMetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event GAMECREATED_EVENT = new Event("GameCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event METADATAUPDATE_EVENT = new Event("MetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected GameNFT(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected GameNFT(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected GameNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected GameNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
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

    public static List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalForAllEventResponse getApprovalForAllEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, log);
        ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalForAllEventFromLog(log));
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public static List<BatchMetadataUpdateEventResponse> getBatchMetadataUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<BatchMetadataUpdateEventResponse> responses = new ArrayList<BatchMetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._fromTokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._toTokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BatchMetadataUpdateEventResponse getBatchMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, log);
        BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._fromTokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse._toTokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBatchMetadataUpdateEventFromLog(log));
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHMETADATAUPDATE_EVENT));
        return batchMetadataUpdateEventFlowable(filter);
    }

    public static List<GameCreatedEventResponse> getGameCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GAMECREATED_EVENT, transactionReceipt);
        ArrayList<GameCreatedEventResponse> responses = new ArrayList<GameCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GameCreatedEventResponse typedResponse = new GameCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.codeHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GameCreatedEventResponse getGameCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GAMECREATED_EVENT, log);
        GameCreatedEventResponse typedResponse = new GameCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.creator = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.codeHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<GameCreatedEventResponse> gameCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGameCreatedEventFromLog(log));
    }

    public Flowable<GameCreatedEventResponse> gameCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GAMECREATED_EVENT));
        return gameCreatedEventFlowable(filter);
    }

    public static List<MetadataUpdateEventResponse> getMetadataUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<MetadataUpdateEventResponse> responses = new ArrayList<MetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MetadataUpdateEventResponse getMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, log);
        MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMetadataUpdateEventFromLog(log));
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(METADATAUPDATE_EVENT));
        return metadataUpdateEventFlowable(filter);
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
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
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
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
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

    public RemoteFunctionCall<TransactionReceipt> adminMint(String to, String title, String description, String codeHash, String tokenURI) {
        final Function function = new Function(
                FUNC_ADMINMINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.Utf8String(codeHash), 
                new org.web3j.abi.datatypes.Utf8String(tokenURI)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createGame(String title, String description, String codeHash, String tokenURI) {
        final Function function = new Function(
                FUNC_CREATEGAME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.Utf8String(codeHash), 
                new org.web3j.abi.datatypes.Utf8String(tokenURI)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> creationFee() {
        final Function function = new Function(FUNC_CREATIONFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> feeRecipient() {
        final Function function = new Function(FUNC_FEERECIPIENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple5<String, String, String, String, BigInteger>> games(BigInteger param0) {
        final Function function = new Function(FUNC_GAMES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, String, String, BigInteger>>(function,
                new Callable<Tuple5<String, String, String, String, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> getApproved(BigInteger tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isApprovedForAll(String owner, String operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.Address(160, operator)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteFunctionCall<String> ownerOf(BigInteger tokenId) {
        final Function function = new Function(FUNC_OWNEROF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> platformToken() {
        final Function function = new Function(FUNC_PLATFORMTOKEN, 
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

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId, byte[] data) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(String operator, Boolean approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, operator), 
                new org.web3j.abi.datatypes.Bool(approved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCreationFee(BigInteger _creationFee) {
        final Function function = new Function(
                FUNC_SETCREATIONFEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_creationFee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFeeRecipient(String _feeRecipient) {
        final Function function = new Function(
                FUNC_SETFEERECIPIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _feeRecipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPlatformToken(String _platformToken) {
        final Function function = new Function(
                FUNC_SETPLATFORMTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _platformToken)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tokenURI(BigInteger tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
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
    public static GameNFT load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameNFT(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static GameNFT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameNFT(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static GameNFT load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new GameNFT(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static GameNFT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new GameNFT(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<GameNFT> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String name_, String symbol_, String _platformToken, String _feeRecipient, BigInteger _creationFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_), 
                new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_creationFee)));
        return deployRemoteCall(GameNFT.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<GameNFT> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name_, String symbol_, String _platformToken, String _feeRecipient, BigInteger _creationFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_), 
                new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_creationFee)));
        return deployRemoteCall(GameNFT.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GameNFT> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_, String _platformToken, String _feeRecipient, BigInteger _creationFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_), 
                new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_creationFee)));
        return deployRemoteCall(GameNFT.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GameNFT> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_, String _platformToken, String _feeRecipient, BigInteger _creationFee) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_), 
                new org.web3j.abi.datatypes.Utf8String(symbol_), 
                new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_creationFee)));
        return deployRemoteCall(GameNFT.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String approved;

        public BigInteger tokenId;
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public String owner;

        public String operator;

        public Boolean approved;
    }

    public static class BatchMetadataUpdateEventResponse extends BaseEventResponse {
        public BigInteger _fromTokenId;

        public BigInteger _toTokenId;
    }

    public static class GameCreatedEventResponse extends BaseEventResponse {
        public BigInteger tokenId;

        public String creator;

        public String codeHash;
    }

    public static class MetadataUpdateEventResponse extends BaseEventResponse {
        public BigInteger _tokenId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger tokenId;
    }
}
