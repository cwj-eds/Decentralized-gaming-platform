package com.decentralized.gaming.platform.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
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
    public static final String BINARY = "0x608034620004af576040906001600160401b0381830181811183821017620003af578352600e82526020906d283630ba3337b936902a37b5b2b760911b8284015283519284840184811083821117620003af5785526003918285526214131560ea1b84860152815192818411620003af578054936001938486811c96168015620004a4575b878710146200048e578190601f9687811162000438575b508790878311600114620003d157600092620003c5575b505060001982841b1c191690841b1781555b8551918211620003af5760049586548481811c91168015620003a4575b878210146200038f5785811162000344575b508590858411600114620002d957938394918492600095620002cd575b50501b92600019911b1c19161783555b600554336001600160a01b0382167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0600080a36001600160a81b0319163360ff60a01b19811691909117600555156200028e57506002546b033b2e3c9fd0803ce8000000928382018092116200027957506000917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9160025533835282815284832084815401905584519384523393a3670de0b6b3a7640000602982516847414d455f504c415960b81b8152600860098201522055678ac7230489e80000602b82516a47414d455f43524541544560a81b81526008600b8201522055674563918244f40000602c82516b4147454e545f43524541544560a01b81526008600c820152205567016345785d8a0000602b82516a2220a4a62cafa627a3a4a760a91b81526008600b82015220555161182c9081620004b58239f35b601190634e487b7160e01b6000525260246000fd5b90606493519262461bcd60e51b845283015260248201527f45524332303a206d696e7420746f20746865207a65726f2061646472657373006044820152fd5b01519350388062000110565b9190601f198416928860005284886000209460005b8a898383106200032c575050501062000311575b50505050811b01835562000120565b01519060f884600019921b161c191690553880808062000302565b868601518955909701969485019488935001620002ee565b87600052866000208680860160051c82019289871062000385575b0160051c019085905b82811062000378575050620000f3565b6000815501859062000368565b925081926200035f565b602288634e487b7160e01b6000525260246000fd5b90607f1690620000e1565b634e487b7160e01b600052604160045260246000fd5b015190503880620000b2565b90869350601f1983169185600052896000209260005b8b82821062000421575050841162000408575b505050811b018155620000c4565b015160001983861b60f8161c19169055388080620003fa565b8385015186558a97909501949384019301620003e7565b90915083600052876000208780850160051c8201928a861062000484575b918891869594930160051c01915b828110620004745750506200009b565b6000815585945088910162000464565b9250819262000456565b634e487b7160e01b600052602260045260246000fd5b95607f169562000084565b600080fdfe608060408181526004918236101561001657600080fd5b600090813560e01c90816303d41e0e146110655750806306fdde0314610f71578063095ea7b314610f475780630cb7d5c214610ee05780630d895ee114610ea05780630e1e1cee14610db557806318160ddd14610d9657806323b872dd14610cc3578063313ce56714610ca75780633950935114610c575780633f4ba83a14610bbb57806342966c6814610b3d5780635c975abb14610b1657806370a0823114610adf578063715018a614610a825780638456cb5914610a215780638da5cb5b146109f857806395d89b41146108de578063979430d214610766578063a457c2d7146106c1578063a9059cbb14610688578063cf456ae714610645578063d3fc98641461054b578063db2e21bc146104fb578063dd62ed3e146104b2578063eccf3b92146102d3578063f2fde38b14610203578063f46eccc4146101c15763f6018da01461016357600080fd5b346101be5760203660031901126101be5782359067ffffffffffffffff82116101be5750602061019a81946101ac93369101611151565b818451938285809451938492016110d1565b81016008815203019020549051908152f35b80fd5b5090346101ff5760203660031901126101ff5760209160ff9082906001600160a01b036101ec6110a0565b1681526006855220541690519015158152f35b5080fd5b5091346102cf5760203660031901126102cf5761021e6110a0565b9061022761121d565b6001600160a01b0391821692831561027d575050600554826bffffffffffffffffffffffff60a01b821617600555167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08380a380f35b906020608492519162461bcd60e51b8352820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152fd5b8280fd5b508290346101be57826003193601126101be5781359167ffffffffffffffff938484116102cf57366023850112156102cf57838201359385851161049f578460051b95825191602096610328888a0185611119565b8352868301602480998301019136831161049b578901905b828210610478575050508635908111610474576103609036908501611151565b943385526006815260ff83862054168015610460575b61037f9061139a565b61038761142b565b82518181885161039a8183858d016110d1565b81016008815203019020546103b081151561161c565b855b835181101561045c576001600160a01b03806103ce838761165e565b51166103f9575b5060001981146103e7576001016103b2565b634e487b7160e01b8752601186528887fd5b61040f8382610408858961165e565b5116611472565b610419828661165e565b51167fa4c898e193f9b90ee1075eb2dc37474589a0bb45bb6307b47ba6d456b00010e886518481528786820152806104538982018d6110f4565b0390a2896103d5565b8680f35b506005546001600160a01b03163314610376565b8480fd5b81356001600160a01b0381168103610497578152908801908801610340565b8880fd5b8780fd5b634e487b7160e01b845260418352602484fd5b5090346101ff57806003193601126101ff57806020926104d06110a0565b6104d86110bb565b6001600160a01b0391821683526001865283832091168252845220549051908152f35b5090346101ff57816003193601126101ff5761051561121d565b8180808060018060a01b03600554164790828215610542575bf115610538575080f35b51903d90823e3d90fd5b506108fc61052e565b509190346102cf5761055c366111d7565b9091338652600660205260ff84872054168015610631575b61057d9061139a565b61058561142b565b6001600160a01b0381169485156105ee575082916105d3836105e8936105ce7f85a66b9141978db9980f7e0ce3b468cebf4f7999f32b23091c5c03e798b1ba7a989715156113df565b611472565b835193849384528060208501528301906110f4565b0390a280f35b606490602086519162461bcd60e51b8352820152601b60248201527f43616e6e6f74206d696e7420746f207a65726f206164647265737300000000006044820152fd5b506005546001600160a01b03163314610574565b5090346101ff5761068590610659366111a8565b919061066361121d565b60018060a01b03168452600660205283209060ff801983541691151516179055565b80f35b5090346101ff57806003193601126101ff576020906106ba6106a86110a0565b6106b061142b565b6024359033611688565b5160018152f35b508290346101be57826003193601126101be576106dc6110a0565b918360243592338152600160205281812060018060a01b0386168252602052205490828210610715576020856106ba8585038733611298565b608490602086519162461bcd60e51b8352820152602560248201527f45524332303a2064656372656173656420616c6c6f77616e63652062656c6f77604482015264207a65726f60d81b6064820152fd5b509190346102cf57610777366111d7565b9091923386526020936007855260ff828820541680156108ca575b1561088e5761079f61142b565b6001600160a01b03811695861561084b576107bb8515156113df565b8688528786528483892054106108115750916105e8916107fe857f47e772fda56eb54ab211642ce5421882c49fc2b7033455982af14588ae4207ff979695611515565b80805195869586528501528301906110f4565b825162461bcd60e51b81529081018690526014602482015273496e73756666696369656e742062616c616e636560601b6044820152606490fd5b825162461bcd60e51b8152908101869052601d60248201527f43616e6e6f74206275726e2066726f6d207a65726f20616464726573730000006044820152606490fd5b815162461bcd60e51b815280870186905260166024820152752737ba1030baba3437b934bd32b2103a3790313ab93760511b6044820152606490fd5b506005546001600160a01b03163314610792565b50346101be57806003193601126101be5781519181845492600184811c918186169586156109ee575b60209687851081146109db579087899a92868b999a9b5291826000146109b1575050600114610956575b858861095289610943848a0385611119565b519282849384528301906110f4565b0390f35b815286935091907f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b5b828410610999575050508201018161094361095238610931565b8054848a01860152889550879490930192810161097f565b60ff19168882015294151560051b8701909401945085935061094392506109529150389050610931565b634e487b7160e01b835260228a52602483fd5b92607f1692610907565b5090346101ff57816003193601126101ff5760055490516001600160a01b039091168152602090f35b5090346101ff57816003193601126101ff5760207f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a25891610a5f61121d565b610a6761142b565b6005805460ff60a01b1916600160a01b17905551338152a180f35b50346101be57806003193601126101be57610a9b61121d565b600580546001600160a01b0319811690915581906001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a380f35b5090346101ff5760203660031901126101ff5760209181906001600160a01b03610b076110a0565b16815280845220549051908152f35b5090346101ff57816003193601126101ff5760209060ff60055460a01c1690519015158152f35b5091346102cf5760203660031901126102cf576009903591610b5d61142b565b610b688315156113df565b610b728333611515565b8051928352806020840152820152682aa9a2a92fa12aa92760b91b60608201527f47e772fda56eb54ab211642ce5421882c49fc2b7033455982af14588ae4207ff60803392a280f35b5091346102cf57826003193601126102cf57610bd561121d565b6005549060ff8260a01c1615610c1d575060ff60a01b1916600555513381527f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa90602090a180f35b606490602084519162461bcd60e51b8352820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b6044820152fd5b5090346101ff57806003193601126101ff576106ba602092610ca0610c7a6110a0565b338352600186528483206001600160a01b03821684528652918490205460243590611275565b9033611298565b5090346101ff57816003193601126101ff576020905160128152f35b5082346101ff5760603660031901126101ff57610cde6110a0565b610ce66110bb565b918460443594610cf461142b565b6001600160a01b03841681526001602081815283832033845290529190205491908201610d2a575b6020866106ba878787611688565b848210610d535750918391610d48602096956106ba95033383611298565b919394819350610d1c565b606490602087519162461bcd60e51b8352820152601d60248201527f45524332303a20696e73756666696369656e7420616c6c6f77616e63650000006044820152fd5b5090346101ff57816003193601126101ff576020906002549051908152f35b5091346102cf57816003193601126102cf57610dcf6110a0565b60243567ffffffffffffffff8111610474577fa4c898e193f9b90ee1075eb2dc37474589a0bb45bb6307b47ba6d456b00010e892610e0f91369101611151565b338552600660205260ff84862054168015610e8c575b610e2e9061139a565b610e3661142b565b6105e884516020818451610e4d81838589016110d1565b810160088152030190205491610e6483151561161c565b610e6e8385611472565b8551938493845286602085015260018060a01b0316958301906110f4565b506005546001600160a01b03163314610e25565b5090346101ff5761068590610eb4366111a8565b9190610ebe61121d565b60018060a01b03168452600760205283209060ff801983541691151516179055565b5091346102cf57816003193601126102cf5780359067ffffffffffffffff8211610f4357610f16610f2d91602093369101611151565b92610f1f61121d565b5192838151938492016110d1565b8101906008825260208160243593030190205580f35b8380fd5b5090346101ff57806003193601126101ff576020906106ba610f676110a0565b6024359033611298565b50346101be57806003193601126101be578151918160035492600184811c9181861695861561105b575b60209687851081146109db578899509688969785829a529182600014611034575050600114610fd8575b5050506109529291610943910385611119565b9190869350600383527fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b5b82841061101c5750505082010181610943610952610fc5565b8054848a018601528895508794909301928101611003565b60ff19168782015293151560051b8601909301935084925061094391506109529050610fc5565b92607f1692610f9b565b905082346102cf5760203660031901126102cf5760209260ff91906001600160a01b036110906110a0565b1681526007855220541615158152f35b600435906001600160a01b03821682036110b657565b600080fd5b602435906001600160a01b03821682036110b657565b60005b8381106110e45750506000910152565b81810151838201526020016110d4565b9060209161110d815180928185528580860191016110d1565b601f01601f1916010190565b90601f8019910116810190811067ffffffffffffffff82111761113b57604052565b634e487b7160e01b600052604160045260246000fd5b81601f820112156110b65780359067ffffffffffffffff821161113b5760405192611186601f8401601f191660200185611119565b828452602083830101116110b657816000926020809301838601378301015290565b60409060031901126110b6576004356001600160a01b03811681036110b6579060243580151581036110b65790565b60606003198201126110b6576004356001600160a01b03811681036110b65791602435916044359067ffffffffffffffff82116110b65761121a91600401611151565b90565b6005546001600160a01b0316330361123157565b606460405162461bcd60e51b815260206004820152602060248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152fd5b9190820180921161128257565b634e487b7160e01b600052601160045260246000fd5b6001600160a01b0390811691821561134957169182156112f95760207f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925918360005260018252604060002085600052825280604060002055604051908152a3565b60405162461bcd60e51b815260206004820152602260248201527f45524332303a20617070726f766520746f20746865207a65726f206164647265604482015261737360f01b6064820152608490fd5b60405162461bcd60e51b8152602060048201526024808201527f45524332303a20617070726f76652066726f6d20746865207a65726f206164646044820152637265737360e01b6064820152608490fd5b156113a157565b60405162461bcd60e51b8152602060048201526016602482015275139bdd08185d5d1a1bdc9a5e9959081d1bc81b5a5b9d60521b6044820152606490fd5b156113e657565b60405162461bcd60e51b815260206004820152601d60248201527f416d6f756e74206d7573742062652067726561746572207468616e20300000006044820152606490fd5b60ff60055460a01c1661143a57565b60405162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b6044820152606490fd5b6001600160a01b03169081156114d0577fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef6020826114b4600094600254611275565b60025584845283825260408420818154019055604051908152a3565b60405162461bcd60e51b815260206004820152601f60248201527f45524332303a206d696e7420746f20746865207a65726f2061646472657373006044820152606490fd5b6001600160a01b031680156115cd5760009181835282602052604083205481811061157d57817fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef926020928587528684520360408620558060025403600255604051908152a3565b60405162461bcd60e51b815260206004820152602260248201527f45524332303a206275726e20616d6f756e7420657863656564732062616c616e604482015261636560f01b6064820152608490fd5b60405162461bcd60e51b815260206004820152602160248201527f45524332303a206275726e2066726f6d20746865207a65726f206164647265736044820152607360f81b6064820152608490fd5b1561162357565b60405162461bcd60e51b8152602060048201526013602482015272496e76616c696420726577617264207479706560681b6044820152606490fd5b80518210156116725760209160051b010190565b634e487b7160e01b600052603260045260246000fd5b6001600160a01b039081169182156117a35716918215611752576000828152806020526040812054918083106116fe57604082827fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef958760209652828652038282205586815220818154019055604051908152a3565b60405162461bcd60e51b815260206004820152602660248201527f45524332303a207472616e7366657220616d6f756e7420657863656564732062604482015265616c616e636560d01b6064820152608490fd5b60405162461bcd60e51b815260206004820152602360248201527f45524332303a207472616e7366657220746f20746865207a65726f206164647260448201526265737360e81b6064820152608490fd5b60405162461bcd60e51b815260206004820152602560248201527f45524332303a207472616e736665722066726f6d20746865207a65726f206164604482015264647265737360d81b6064820152608490fdfea26469706673582212200957baba1a06cb021910cb032bacc5b20b07d0d30efc3beb0a3724fb0216f53c64736f6c63430008130033";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BATCHREWARD = "batchReward";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_BURNFROM = "burnFrom";

    public static final String FUNC_BURNERS = "burners";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_EMERGENCYWITHDRAW = "emergencyWithdraw";

    public static final String FUNC_GIVEREWARD = "giveReward";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_MINTERS = "minters";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REWARDAMOUNTS = "rewardAmounts";

    public static final String FUNC_SETBURNER = "setBurner";

    public static final String FUNC_SETMINTER = "setMinter";

    public static final String FUNC_SETREWARDAMOUNT = "setRewardAmount";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BURN_EVENT = new Event("Burn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event MINT_EVENT = new Event("Mint", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event REWARD_EVENT = new Event("Reward", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
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

    public static List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BURN_EVENT, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BurnEventResponse getBurnEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BURN_EVENT, log);
        BurnEventResponse typedResponse = new BurnEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BurnEventResponse> burnEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBurnEventFromLog(log));
    }

    public Flowable<BurnEventResponse> burnEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BURN_EVENT));
        return burnEventFlowable(filter);
    }

    public static List<MintEventResponse> getMintEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MINT_EVENT, transactionReceipt);
        ArrayList<MintEventResponse> responses = new ArrayList<MintEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MintEventResponse typedResponse = new MintEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MintEventResponse getMintEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MINT_EVENT, log);
        MintEventResponse typedResponse = new MintEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<MintEventResponse> mintEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMintEventFromLog(log));
    }

    public Flowable<MintEventResponse> mintEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINT_EVENT));
        return mintEventFlowable(filter);
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

    public static List<PausedEventResponse> getPausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PausedEventResponse getPausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PAUSED_EVENT, log);
        PausedEventResponse typedResponse = new PausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPausedEventFromLog(log));
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventFlowable(filter);
    }

    public static List<RewardEventResponse> getRewardEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REWARD_EVENT, transactionReceipt);
        ArrayList<RewardEventResponse> responses = new ArrayList<RewardEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RewardEventResponse typedResponse = new RewardEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.rewardType = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RewardEventResponse getRewardEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(REWARD_EVENT, log);
        RewardEventResponse typedResponse = new RewardEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.rewardType = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<RewardEventResponse> rewardEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRewardEventFromLog(log));
    }

    public Flowable<RewardEventResponse> rewardEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REWARD_EVENT));
        return rewardEventFlowable(filter);
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

    public static List<UnpausedEventResponse> getUnpausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UnpausedEventResponse getUnpausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(UNPAUSED_EVENT, log);
        UnpausedEventResponse typedResponse = new UnpausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUnpausedEventFromLog(log));
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.Address(160, spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger amount) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> batchReward(List<String> recipients, String rewardType) {
        final Function function = new Function(
                FUNC_BATCHREWARD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Utf8String(rewardType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burn(BigInteger amount) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnFrom(String from, BigInteger amount, String reason) {
        final Function function = new Function(
                FUNC_BURNFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Utf8String(reason)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> burners(String param0) {
        final Function function = new Function(FUNC_BURNERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> decreaseAllowance(String spender, BigInteger subtractedValue) {
        final Function function = new Function(
                FUNC_DECREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender), 
                new org.web3j.abi.datatypes.generated.Uint256(subtractedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> emergencyWithdraw() {
        final Function function = new Function(
                FUNC_EMERGENCYWITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> giveReward(String to, String rewardType) {
        final Function function = new Function(
                FUNC_GIVEREWARD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.Utf8String(rewardType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> increaseAllowance(String spender, BigInteger addedValue) {
        final Function function = new Function(
                FUNC_INCREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender), 
                new org.web3j.abi.datatypes.generated.Uint256(addedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(String to, BigInteger amount, String reason) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Utf8String(reason)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> minters(String param0) {
        final Function function = new Function(FUNC_MINTERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
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

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> rewardAmounts(String param0) {
        final Function function = new Function(FUNC_REWARDAMOUNTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setBurner(String account, Boolean canBurn) {
        final Function function = new Function(
                FUNC_SETBURNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account), 
                new org.web3j.abi.datatypes.Bool(canBurn)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setMinter(String account, Boolean canMint) {
        final Function function = new Function(
                FUNC_SETMINTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account), 
                new org.web3j.abi.datatypes.Bool(canMint)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setRewardAmount(String rewardType, BigInteger amount) {
        final Function function = new Function(
                FUNC_SETREWARDAMOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(rewardType), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
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

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
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

    public RemoteFunctionCall<TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
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

    public static RemoteCall<PlatformToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PlatformToken.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<PlatformToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PlatformToken.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PlatformToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PlatformToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PlatformToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PlatformToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class BurnEventResponse extends BaseEventResponse {
        public String from;

        public BigInteger amount;

        public String reason;
    }

    public static class MintEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger amount;

        public String reason;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class RewardEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger amount;

        public String rewardType;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }
}
