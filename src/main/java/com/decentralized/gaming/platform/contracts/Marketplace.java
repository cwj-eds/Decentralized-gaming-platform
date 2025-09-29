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
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
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
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple9;
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
public class Marketplace extends Contract {
    public static final String BINARY = "0x6080346200017a57601f6200227538819003918201601f19168301916001600160401b038311848410176200017f5780849260409485528339810103126200017a576200005a6020620000528362000195565b920162000195565b906001600055600154916040519260018060a01b03928391338382167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0600080a36001600160a81b0319163360ff60a01b19161760015560fa6003551692831562000138575016908115620000f35760018060a01b03199081600254161760025560045416176004556040516120ca9081620001ab8239f35b60405162461bcd60e51b815260206004820152601d60248201527f496e76616c69642066656520726563697069656e7420616464726573730000006044820152606490fd5b62461bcd60e51b815260206004820152601e60248201527f496e76616c696420706c6174666f726d20746f6b656e206164647265737300006044820152606490fd5b600080fd5b634e487b7160e01b600052604160045260246000fd5b51906001600160a01b03821682036200017a5756fe6040608081526004908136101561001557600080fd5b60009160e08335811c908163054564f014611bcb578163107a274a14611af157816314e887e814611ad157816326e27c3e14611a78578163305a67a81461192f57816333ea3dc81461180d5781633f4ba83a1461177157816343f727761461172857816345596e2e1461169057816346904840146116685781635c975abb146116405781635f42da09146115cb5781636c2c9c7d146115ab578163715018a61461154d57816377420844146114d9578163778c4e03146110a257816382367b2d14610fd45781638350908014610f5e5781638456cb5914610efc5781638da5cb5b14610ed2578163978bbdb914610eb25781639ace38c214610e495781639eea4a1314610c3557848263b1a8129514610b9d57508163b842ec4414610b43578163d17ade1a14610ace578163d1b812cd14610aa4578163d5b9221b14610a65578163de74e57b146109c457508063df923dd11461094e578063e74b981b14610885578063e7fb74c71461034d578063ef925399146102cb578063f2fa7392146102715763f2fde38b146101a757600080fd5b3461026d57602036600319011261026d576101c0611c14565b906101c9611dfb565b6001600160a01b0391821692831561021b575050600154826001600160601b0360a01b821617600155167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0600080a380f35b906020608492519162461bcd60e51b8352820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b6064820152fd5b8280fd5b5050346102c757806003193601126102c75761028b611c14565b90602435918215158093036102c3576102a2611dfb565b60018060a01b03168352600a60205282209060ff8019835416911617905580f35b8380fd5b5080fd5b5050346102c7576020908160031936011261026d576001600160a01b036102f0611c14565b168352600d82528083209281518093808654928381520195835280832092905b828210610336576103328686610328828b0383611dc1565b5191829182611cef565b0390f35b835487529586019560019384019390910190610310565b50903461026d57602091826003193601126102c357803590600292838654146108435783865561037b611f35565b82151580610837575b61038d90611f7c565b828652600585526103b360ff6005838920015460081c166103ad81611c5d565b15611fbd565b8286526005855280862060018101805491956001600160a01b039283169291903384146107fe578588015497600354808a02908a8204148a1517156107eb57612710900494858a03908a82116107d85785845416908d8d8a51906370a0823160e01b8252338d83015281602481875afa9182156107cd57908e92610798575b501061075e57899261047c928f928f938c518096819582946323b872dd60e01b9a8b85523390850160409194939294606082019560018060a01b0380921683521660208201520152565b03925af19081156106f4578d91610741575b50156106fe57908b918b878061062f575b5050858483015416906003830154823b1561062b57895191825230828c0190815233602082015260408101919091529091849183919082908490829060600103925af1801561062157918c9493918c93610603575b50600593927fd2b8648ec6ff6bd9ed10162d6ec424ae792c12820a475e8ef52f23fd7da4f1eb9a9282868b940161010061ff00198254161790556007429101558c89610541600c54611ed6565b9687600c558d828c541694600b82519c8d9661055c88611da5565b875260a081880197338952858101998a52606081019a8b52608081019b8c52019b428d528352522099518a558160018b01935116926001600160601b0360a01b9384825416179055890192511690825416179055516003860155519084015551910155338952600d88526105d5848a20600c5490611efb565b818154168952600d88526105ee848a20600c5490611efb565b54169582519586528501523393a46001815580f35b82955061061291949350611d91565b61026d57908a928a91386104f4565b88513d85823e3d90fd5b8480fd5b85548b548b5185815233818f01908152918b166001600160a01b0316602083015260408201939093529495939491938492918a1691839190829060600103925af19081156106f4578d916106c7575b501561068e57908b918b8761049f565b865162461bcd60e51b81528089018c90526013602482015272119959481d1c985b9cd9995c8819985a5b1959606a1b6044820152606490fd5b6106e791508c8d3d106106ed575b6106df8183611dc1565b810190611ebe565b3861067e565b503d6106d5565b88513d8f823e3d90fd5b865162461bcd60e51b81528089018c9052601960248201527f5472616e7366657220746f2073656c6c6572206661696c6564000000000000006044820152606490fd5b61075891508c8d3d106106ed576106df8183611dc1565b3861048e565b885162461bcd60e51b8152808b018e90526014602482015273496e73756666696369656e742062616c616e636560601b6044820152606490fd5b8f8193508092503d83116107c6575b6107b18183611dc1565b810103126107c2578c905138610432565b8e80fd5b503d6107a7565b8b51903d90823e3d90fd5b634e487b7160e01b8d526011895260248dfd5b634e487b7160e01b8c526011885260248cfd5b845162461bcd60e51b81528087018a9052601360248201527243616e6e6f7420627579206f776e206974656d60681b6044820152606490fd5b50600654831115610384565b5162461bcd60e51b8152908101849052601f60248201527f5265656e7472616e637947756172643a207265656e7472616e742063616c6c006044820152606490fd5b508290346102c75760203660031901126102c7576108a1611c14565b906108aa611dfb565b6001600160a01b03918083169182156109135780546001600160a01b03198116939093179055935191166001600160a01b0390811682529092166020830152907faaebcf1bfa00580e41d966056b48521fa9f202645c86d4ddf28113e617c1b1d390604090a180f35b606490602087519162461bcd60e51b83528201526015602482015274125b9d985b1a5908199959481c9958da5c1a595b9d605a1b6044820152fd5b50903461026d57602091826003193601126102c3573560038110156102c35761097681611c5d565b8352600982528083209281518093808654928381520195835280832092905b8282106109ad576103328686610328828b0383611dc1565b835487529586019560019384019390910190610995565b9050346102c35760203660031901126102c3578261012094833581526005602052209081549360018060a01b03938460018501541694600285015416906003850154908501549060058601549260ff8085169460081c1694600760068901549801549881519a8b5260208b015289015260608801526080870152610a4781611c5d565b60a0860152610a5581611c5d565b60c0850152830152610100820152f35b505050346102c75760203660031901126102c75760209160ff9082906001600160a01b03610a91611c14565b168152600a855220541690519015158152f35b505050346102c757816003193601126102c75760025490516001600160a01b039091168152602090f35b505050346102c7576020908160031936011261026d576001600160a01b03610af4611c14565b168352600882528083209281518093808654928381520195835280832092905b828210610b2c576103328686610328828b0383611dc1565b835487529586019560019384019390910190610b14565b505050346102c757806003193601126102c757610b5e611c14565b6001600160a01b03168252600d60205280822080546024359390841015610b9a5750602092610b8c91611c2f565b91905490519160031b1c8152f35b80fd5b80848634610c3157610bae36611d2a565b9391610bb8611dfb565b6001600160a01b0316803b15610c2d5783516323b872dd60e01b8152309381019384526001600160a01b0390951660208401526040830191909152849184919082908490829060600103925af1908115610c245750610c145750f35b610c1d90611d91565b610b9a5780f35b513d84823e3d90fd5b8580fd5b5050fd5b82848634610b9a5760209081600319360112610b9a5783359267ffffffffffffffff84116102c757366023850112156102c7578385013595610c7687611de3565b94610c8383519687611dc1565b87865284860160246005998a1b83010191368311610c2d576024879101915b838310610e395750505050845196610cb988611de3565b97610cc68451998a611dc1565b808952610cd5601f1991611de3565b0185855b828110610e2357505050835b8651811015610dd55780610cfc610d0e9289612080565b51151580610dbf575b610d1357611ed6565b610ce5565b610d1d8189612080565b5186528287528486206007865191610d3483611d5e565b8054835260018101546001600160a01b039081168b85015260028201541688840152600381015460608401528b81015460808401528581015460ff90818116610d7c81611c5d565b60a086015260081c16610d8e81611c5d565b60c08401526006810154878401520154610100820152610dae828c612080565b52610db9818b612080565b50611ed6565b50610dca8189612080565b516006541015610d05565b505081518481528751818601819052858901959194938501939092505b828110610dff5784840385f35b9091928261012082610e146001948a51611c7d565b01960191019492919094610df2565b610e2b61203b565b82828d010152018690610cd9565b8235815291810191879101610ca2565b5050903461026d57602036600319011261026d578060c09383358152600b6020522080549260018060a01b0392836001840154169360028401541690600560038501549385015494015494815196875260208701528501526060840152608083015260a0820152f35b505050346102c757816003193601126102c7576020906003549051908152f35b505050346102c757816003193601126102c75760015490516001600160a01b039091168152602090f35b505050346102c757816003193601126102c75760207f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a25891610f3b611dfb565b610f43611f35565b6001805460ff60a01b1916600160a01b17905551338152a180f35b5050903461026d576020836044610f7436611d2a565b610f82979297949194611dfb565b8651978895869463a9059cbb60e01b865260018060a01b03809416908601526024850152165af1908115610c245750610fb9575080f35b610fd09060203d81116106ed576106df8183611dc1565b5080f35b5050903461026d578060031936011261026d577f15819dd2fd9f6418b142e798d08a18d0bf06ea368f4480b7b0d3f75bd966bc4890823592602435848652600560205261103060018060a01b0360018589200154163314611ffe565b84151580611096575b61104290611f7c565b848652600560205261106360ff6005858920015460081c166103ad81611c5d565b61106e811515611e72565b848652600560205282862091820191818354935560074291015582519182526020820152a280f35b50600654851115611039565b8484843461026d57608036600319011261026d576110be611c14565b6024946044359486359260643592600384101561026d576110dd611f35565b60018060a01b0380921695868452602099600a8b5260ff8986205416156114975788516331a9108f60e11b81528281018890528b8183818c5afa90811561141157869161147a575b5084339116036114485761113a8a1515611e72565b885163020604bf60e21b81528281018890528b8183818c5afa90811561141157869161141b575b508416301480156113b8575b156113765750863b156102c35787516323b872dd60e01b815233828201908152306020820152604081018890528590829081906060010381838c5af1801561136c578b878b958d98958b98958d95611342575b506006546111cd90611ed6565b9e8f998a96876006558a51986111e28a611d5e565b888a52878a01903382528c8b0192835260608b0193845260808b0194855260a08b019761120e81611c5d565b885260c08b01968c88528b0198428a526101008c019a428c528d52600590528c8c209a518b558060018c01925116916001600160601b0360a01b928382541617905560028b019251169082541617905551600388015551908601556005850191519061127982611c5d565b61128282611c5d565b8254905161128f81611c5d565b61129881611c5d565b60081b61ff00169160ff169061ffff19161717905551600683015551906007015533815260078a5282828220906112ce91611efb565b86815260088a5282828220906112e391611efb565b6112ec85611c5d565b84815260098a5220906112fe91611efb565b84519283528683015261131081611c5d565b8382015283339160607f162821570be8952c6f5330aca077fbcdc4c4062e0e7f98b2ab188738732737a891a451908152f35b9350959850955050869295506113589150611d91565b6102c357928592878b878b958d98386111c0565b89513d87823e3d90fd5b885162461bcd60e51b81529182018b90526018908201527f4d61726b6574706c616365206e6f7420617070726f76656400000000000000006044820152606490fd5b50885163e985e9c560e01b8152338382019081523060208201528c908290819060400103818c5afa9081156114115786916113f4575b5061116d565b61140b91508c8d3d106106ed576106df8183611dc1565b8c6113ee565b8a513d88823e3d90fd5b61143b91508c8d3d10611441575b6114338183611dc1565b810190611e53565b8c611161565b503d611429565b885162461bcd60e51b81529182018b9052600d908201526c2737ba103a34329037bbb732b960991b6044820152606490fd5b61149191508c8d3d10611441576114338183611dc1565b8c611125565b885162461bcd60e51b81529182018b90526017908201527f436f6e7472616374206e6f7420617574686f72697a65640000000000000000006044820152606490fd5b505050346102c757816003193601126102c75760065491908160015b84811115611507576020848451908152f35b80825260ff600580602052848420015460081c1661152481611c5d565b15611538575b61153390611ed6565b6114f5565b9261154561153391611ed6565b93905061152a565b8434610b9a5780600319360112610b9a57611566611dfb565b600180546001600160a01b031981169091556000906001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a380f35b505050346102c757816003193601126102c7576020906006549051908152f35b505050346102c7576020908160031936011261026d576001600160a01b036115f1611c14565b168352600782528083209281518093808654928381520195835280832092905b828210611629576103328686610328828b0383611dc1565b835487529586019560019384019390910190611611565b505050346102c757816003193601126102c75760209060ff60015460a01c1690519015158152f35b50503461026d578260031936011261026d575490516001600160a01b03909116815260209150f35b50503461026d57602036600319011261026d578035906116ae611dfb565b6103e882116116f15750907f14914da2bf76024616fbe1859783fcd4dbddcb179b1f3a854949fbf920dcb95791600354908060035582519182526020820152a180f35b606490602084519162461bcd60e51b8352820152601160248201527008ccaca40e4c2e8ca40e8dede40d0d2ced607b1b6044820152fd5b505050346102c757806003193601126102c757611743611c14565b6001600160a01b03168252600860205280822080546024359390841015610b9a5750602092610b8c91611c2f565b50503461026d578260031936011261026d5761178b611dfb565b6001549060ff8260a01c16156117d3575060ff60a01b1916600155513381527f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa90602090a180f35b606490602084519162461bcd60e51b8352820152601460248201527314185d5cd8589b194e881b9bdd081c185d5cd95960621b6044820152fd5b5050903461026d57602091826003193601126102c35780358460a0845161183381611da5565b82815282878201528286820152826060820152826080820152015280151580611923575b156118e85784839160c09652600b8552209082519261187584611da5565b82549485855260018060a01b0390816001860154169080870191825282806002880154169285890193845260a0600560038a01549860608c01998a528a01549960808c019a8b52015499019889528551998a525116908801525116908501525160608401525160808301525160a0820152f35b5082606492519162461bcd60e51b83528201526016602482015275125b9d985b1a59081d1c985b9cd858dd1a5bdb88125160521b6044820152fd5b50600c54811115611857565b5050903461026d57602036600319011261026d57813591828452600560205260018060a01b03916119698360018388200154163314611ffe565b83151580611a6c575b61197b90611f7c565b838552600560205261199c60ff6005838820015460081c166103ad81611c5d565b83855260056020528481812093806002860154169060018601541690600386015491813b156102c35784516323b872dd60e01b8152309681019687526001600160a01b0390911660208701526040860192909252909384919082908490829060600103925af1908115611a635750611a50575b506005810161020061ff001982541617905560074291015533907ff69c7ea3f71dad86e17ba97a88b7e8f1708c0946bd98c7584e9942f60da8728e8380a380f35b611a5c90939193611d91565b9138611a0f565b513d86823e3d90fd5b50600654841115611972565b50503461026d578160031936011261026d5735916003831015610b9a5760243592611aa281611c5d565b81526009602052818120908154841015610b9a5750602092611ac391611c2f565b90549060031b1c9051908152f35b505050346102c757816003193601126102c757602090600c549051908152f35b84939150346102c35760203660031901126102c357906007611bbd9282610120968635611b1c61203b565b5080151580611bbf575b611b2f90611f7c565b815260056020522090835195611b4487611d5e565b8254875260018301546001600160a01b03908116602089015260028401541685880152600383015460608801528201546080870152600582015460ff90808216611b8d81611c5d565b60a089015260081c16611b9f81611c5d565b60c08701526006820154908601520154610100840152518092611c7d565bf35b50600654811115611b26565b505050346102c757806003193601126102c757611be6611c14565b6001600160a01b03168252600760205280822080546024359390841015610b9a5750602092610b8c91611c2f565b600435906001600160a01b0382168203611c2a57565b600080fd5b8054821015611c475760005260206000200190600090565b634e487b7160e01b600052603260045260246000fd5b60031115611c6757565b634e487b7160e01b600052602160045260246000fd5b8051825260018060a01b038060208301511660208401526040820151166040830152606081015160608301526080810151608083015260a0810151611cc181611c5d565b60a083015260c0810151611cd481611c5d565b60c083015260e081015160e083015261010080910151910152565b6020908160408183019282815285518094520193019160005b828110611d16575050505090565b835185529381019392810192600101611d08565b6060906003190112611c2a576001600160a01b036004358181168103611c2a5791602435916044359081168103611c2a5790565b610120810190811067ffffffffffffffff821117611d7b57604052565b634e487b7160e01b600052604160045260246000fd5b67ffffffffffffffff8111611d7b57604052565b60c0810190811067ffffffffffffffff821117611d7b57604052565b90601f8019910116810190811067ffffffffffffffff821117611d7b57604052565b67ffffffffffffffff8111611d7b5760051b60200190565b6001546001600160a01b03163303611e0f57565b606460405162461bcd60e51b815260206004820152602060248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e65726044820152fd5b90816020910312611c2a57516001600160a01b0381168103611c2a5790565b15611e7957565b60405162461bcd60e51b815260206004820152601c60248201527f5072696365206d7573742062652067726561746572207468616e2030000000006044820152606490fd5b90816020910312611c2a57518015158103611c2a5790565b6000198114611ee55760010190565b634e487b7160e01b600052601160045260246000fd5b805468010000000000000000811015611d7b57611f1d91600182018155611c2f565b819291549060031b91821b91600019901b1916179055565b60ff60015460a01c16611f4457565b60405162461bcd60e51b815260206004820152601060248201526f14185d5cd8589b194e881c185d5cd95960821b6044820152606490fd5b15611f8357565b60405162461bcd60e51b8152602060048201526012602482015271125b9d985b1a59081b1a5cdd1a5b99c8125160721b6044820152606490fd5b15611fc457565b60405162461bcd60e51b81526020600482015260126024820152714c697374696e67206e6f742061637469766560701b6044820152606490fd5b1561200557565b60405162461bcd60e51b815260206004820152600e60248201526d2737ba103a34329039b2b63632b960911b6044820152606490fd5b6040519061204882611d5e565b816101006000918281528260208201528260408201528260608201528260808201528260a08201528260c08201528260e08201520152565b8051821015611c475760209160051b01019056fea264697066735822122045408416d8b3e9d6bbdb03cda769ee56f247b6e439b8442e6a8d9400faa7927b64736f6c63430008130033";

    public static final String FUNC_AUTHORIZEDCONTRACTS = "authorizedContracts";

    public static final String FUNC_BUYITEM = "buyItem";

    public static final String FUNC_CANCELLISTING = "cancelListing";

    public static final String FUNC_CONTRACTLISTINGS = "contractListings";

    public static final String FUNC_EMERGENCYWITHDRAWNFT = "emergencyWithdrawNFT";

    public static final String FUNC_EMERGENCYWITHDRAWTOKEN = "emergencyWithdrawToken";

    public static final String FUNC_FEERATE = "feeRate";

    public static final String FUNC_FEERECIPIENT = "feeRecipient";

    public static final String FUNC_GETACTIVELISTINGCOUNT = "getActiveListingCount";

    public static final String FUNC_GETCONTRACTLISTINGS = "getContractListings";

    public static final String FUNC_GETLISTING = "getListing";

    public static final String FUNC_GETLISTINGSBATCH = "getListingsBatch";

    public static final String FUNC_GETSELLERLISTINGS = "getSellerListings";

    public static final String FUNC_GETTRANSACTION = "getTransaction";

    public static final String FUNC_GETTYPELISTINGS = "getTypeListings";

    public static final String FUNC_GETUSERTRANSACTIONS = "getUserTransactions";

    public static final String FUNC_LISTITEM = "listItem";

    public static final String FUNC_LISTINGCOUNTER = "listingCounter";

    public static final String FUNC_LISTINGS = "listings";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_PLATFORMTOKEN = "platformToken";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SELLERLISTINGS = "sellerListings";

    public static final String FUNC_SETAUTHORIZEDCONTRACT = "setAuthorizedContract";

    public static final String FUNC_SETFEERATE = "setFeeRate";

    public static final String FUNC_SETFEERECIPIENT = "setFeeRecipient";

    public static final String FUNC_TRANSACTIONCOUNTER = "transactionCounter";

    public static final String FUNC_TRANSACTIONS = "transactions";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_TYPELISTINGS = "typeListings";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_UPDATEPRICE = "updatePrice";

    public static final String FUNC_USERTRANSACTIONS = "userTransactions";

    public static final Event FEERATEUPDATED_EVENT = new Event("FeeRateUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FEERECIPIENTUPDATED_EVENT = new Event("FeeRecipientUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ITEMCANCELLED_EVENT = new Event("ItemCancelled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ITEMLISTED_EVENT = new Event("ItemListed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event ITEMSOLD_EVENT = new Event("ItemSold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event PRICEUPDATED_EVENT = new Event("PriceUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Marketplace(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Marketplace(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Marketplace(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Marketplace(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<FeeRateUpdatedEventResponse> getFeeRateUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FEERATEUPDATED_EVENT, transactionReceipt);
        ArrayList<FeeRateUpdatedEventResponse> responses = new ArrayList<FeeRateUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FeeRateUpdatedEventResponse typedResponse = new FeeRateUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldRate = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newRate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FeeRateUpdatedEventResponse getFeeRateUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FEERATEUPDATED_EVENT, log);
        FeeRateUpdatedEventResponse typedResponse = new FeeRateUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.oldRate = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newRate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<FeeRateUpdatedEventResponse> feeRateUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFeeRateUpdatedEventFromLog(log));
    }

    public Flowable<FeeRateUpdatedEventResponse> feeRateUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FEERATEUPDATED_EVENT));
        return feeRateUpdatedEventFlowable(filter);
    }

    public static List<FeeRecipientUpdatedEventResponse> getFeeRecipientUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FEERECIPIENTUPDATED_EVENT, transactionReceipt);
        ArrayList<FeeRecipientUpdatedEventResponse> responses = new ArrayList<FeeRecipientUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FeeRecipientUpdatedEventResponse typedResponse = new FeeRecipientUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldRecipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newRecipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FeeRecipientUpdatedEventResponse getFeeRecipientUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FEERECIPIENTUPDATED_EVENT, log);
        FeeRecipientUpdatedEventResponse typedResponse = new FeeRecipientUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.oldRecipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newRecipient = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<FeeRecipientUpdatedEventResponse> feeRecipientUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFeeRecipientUpdatedEventFromLog(log));
    }

    public Flowable<FeeRecipientUpdatedEventResponse> feeRecipientUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FEERECIPIENTUPDATED_EVENT));
        return feeRecipientUpdatedEventFlowable(filter);
    }

    public static List<ItemCancelledEventResponse> getItemCancelledEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ITEMCANCELLED_EVENT, transactionReceipt);
        ArrayList<ItemCancelledEventResponse> responses = new ArrayList<ItemCancelledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ItemCancelledEventResponse typedResponse = new ItemCancelledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.seller = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ItemCancelledEventResponse getItemCancelledEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ITEMCANCELLED_EVENT, log);
        ItemCancelledEventResponse typedResponse = new ItemCancelledEventResponse();
        typedResponse.log = log;
        typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.seller = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<ItemCancelledEventResponse> itemCancelledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getItemCancelledEventFromLog(log));
    }

    public Flowable<ItemCancelledEventResponse> itemCancelledEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ITEMCANCELLED_EVENT));
        return itemCancelledEventFlowable(filter);
    }

    public static List<ItemListedEventResponse> getItemListedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ITEMLISTED_EVENT, transactionReceipt);
        ArrayList<ItemListedEventResponse> responses = new ArrayList<ItemListedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ItemListedEventResponse typedResponse = new ItemListedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.seller = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.nftContract = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.itemType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ItemListedEventResponse getItemListedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ITEMLISTED_EVENT, log);
        ItemListedEventResponse typedResponse = new ItemListedEventResponse();
        typedResponse.log = log;
        typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.seller = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.nftContract = (String) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.itemType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ItemListedEventResponse> itemListedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getItemListedEventFromLog(log));
    }

    public Flowable<ItemListedEventResponse> itemListedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ITEMLISTED_EVENT));
        return itemListedEventFlowable(filter);
    }

    public static List<ItemSoldEventResponse> getItemSoldEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ITEMSOLD_EVENT, transactionReceipt);
        ArrayList<ItemSoldEventResponse> responses = new ArrayList<ItemSoldEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ItemSoldEventResponse typedResponse = new ItemSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.buyer = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.seller = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ItemSoldEventResponse getItemSoldEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ITEMSOLD_EVENT, log);
        ItemSoldEventResponse typedResponse = new ItemSoldEventResponse();
        typedResponse.log = log;
        typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.buyer = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.seller = (String) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<ItemSoldEventResponse> itemSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getItemSoldEventFromLog(log));
    }

    public Flowable<ItemSoldEventResponse> itemSoldEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ITEMSOLD_EVENT));
        return itemSoldEventFlowable(filter);
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

    public static List<PriceUpdatedEventResponse> getPriceUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRICEUPDATED_EVENT, transactionReceipt);
        ArrayList<PriceUpdatedEventResponse> responses = new ArrayList<PriceUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PriceUpdatedEventResponse typedResponse = new PriceUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oldPrice = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPrice = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PriceUpdatedEventResponse getPriceUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRICEUPDATED_EVENT, log);
        PriceUpdatedEventResponse typedResponse = new PriceUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.listingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.oldPrice = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newPrice = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<PriceUpdatedEventResponse> priceUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPriceUpdatedEventFromLog(log));
    }

    public Flowable<PriceUpdatedEventResponse> priceUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRICEUPDATED_EVENT));
        return priceUpdatedEventFlowable(filter);
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

    public RemoteFunctionCall<Boolean> authorizedContracts(String param0) {
        final Function function = new Function(FUNC_AUTHORIZEDCONTRACTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> buyItem(BigInteger listingId) {
        final Function function = new Function(
                FUNC_BUYITEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(listingId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> cancelListing(BigInteger listingId) {
        final Function function = new Function(
                FUNC_CANCELLISTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(listingId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> contractListings(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_CONTRACTLISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> emergencyWithdrawNFT(String nftContract, BigInteger tokenId, String to) {
        final Function function = new Function(
                FUNC_EMERGENCYWITHDRAWNFT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, nftContract), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.Address(160, to)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> emergencyWithdrawToken(String token, BigInteger amount, String to) {
        final Function function = new Function(
                FUNC_EMERGENCYWITHDRAWTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, token), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Address(160, to)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> feeRate() {
        final Function function = new Function(FUNC_FEERATE, 
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

    public RemoteFunctionCall<BigInteger> getActiveListingCount() {
        final Function function = new Function(FUNC_GETACTIVELISTINGCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getContractListings(String nftContract) {
        final Function function = new Function(FUNC_GETCONTRACTLISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, nftContract)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Listing> getListing(BigInteger listingId) {
        final Function function = new Function(FUNC_GETLISTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(listingId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Listing>() {}));
        return executeRemoteCallSingleValueReturn(function, Listing.class);
    }

    public RemoteFunctionCall<List> getListingsBatch(List<BigInteger> listingIds) {
        final Function function = new Function(FUNC_GETLISTINGSBATCH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(listingIds, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Listing>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getSellerListings(String seller) {
        final Function function = new Function(FUNC_GETSELLERLISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, seller)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Transaction> getTransaction(BigInteger transactionId) {
        final Function function = new Function(FUNC_GETTRANSACTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(transactionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Transaction>() {}));
        return executeRemoteCallSingleValueReturn(function, Transaction.class);
    }

    public RemoteFunctionCall<List> getTypeListings(BigInteger itemType) {
        final Function function = new Function(FUNC_GETTYPELISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(itemType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getUserTransactions(String user) {
        final Function function = new Function(FUNC_GETUSERTRANSACTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> listItem(String nftContract, BigInteger tokenId, BigInteger price, BigInteger itemType) {
        final Function function = new Function(
                FUNC_LISTITEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, nftContract), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.generated.Uint256(price), 
                new org.web3j.abi.datatypes.generated.Uint8(itemType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> listingCounter() {
        final Function function = new Function(FUNC_LISTINGCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple9<BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> listings(BigInteger param0) {
        final Function function = new Function(FUNC_LISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple9<BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple9<BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple9<BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple9<BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (BigInteger) results.get(8).getValue());
                    }
                });
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

    public RemoteFunctionCall<BigInteger> sellerListings(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_SELLERLISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setAuthorizedContract(String nftContract, Boolean authorized) {
        final Function function = new Function(
                FUNC_SETAUTHORIZEDCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, nftContract), 
                new org.web3j.abi.datatypes.Bool(authorized)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFeeRate(BigInteger newFeeRate) {
        final Function function = new Function(
                FUNC_SETFEERATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(newFeeRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFeeRecipient(String newFeeRecipient) {
        final Function function = new Function(
                FUNC_SETFEERECIPIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newFeeRecipient)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> transactionCounter() {
        final Function function = new Function(FUNC_TRANSACTIONCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>> transactions(BigInteger param0) {
        final Function function = new Function(FUNC_TRANSACTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, String, String, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> typeListings(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_TYPELISTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updatePrice(BigInteger listingId, BigInteger newPrice) {
        final Function function = new Function(
                FUNC_UPDATEPRICE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(listingId), 
                new org.web3j.abi.datatypes.generated.Uint256(newPrice)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> userTransactions(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_USERTRANSACTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Marketplace load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Marketplace(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Marketplace load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Marketplace(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Marketplace load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Marketplace(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Marketplace load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Marketplace(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Marketplace> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _platformToken, String _feeRecipient) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient)));
        return deployRemoteCall(Marketplace.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Marketplace> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _platformToken, String _feeRecipient) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient)));
        return deployRemoteCall(Marketplace.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Marketplace> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _platformToken, String _feeRecipient) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient)));
        return deployRemoteCall(Marketplace.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Marketplace> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _platformToken, String _feeRecipient) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _platformToken), 
                new org.web3j.abi.datatypes.Address(160, _feeRecipient)));
        return deployRemoteCall(Marketplace.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class Listing extends StaticStruct {
        public BigInteger listingId;

        public String seller;

        public String nftContract;

        public BigInteger tokenId;

        public BigInteger price;

        public BigInteger itemType;

        public BigInteger status;

        public BigInteger createdAt;

        public BigInteger updatedAt;

        public Listing(BigInteger listingId, String seller, String nftContract, BigInteger tokenId, BigInteger price, BigInteger itemType, BigInteger status, BigInteger createdAt, BigInteger updatedAt) {
            super(new org.web3j.abi.datatypes.generated.Uint256(listingId), 
                    new org.web3j.abi.datatypes.Address(160, seller), 
                    new org.web3j.abi.datatypes.Address(160, nftContract), 
                    new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                    new org.web3j.abi.datatypes.generated.Uint256(price), 
                    new org.web3j.abi.datatypes.generated.Uint8(itemType), 
                    new org.web3j.abi.datatypes.generated.Uint8(status), 
                    new org.web3j.abi.datatypes.generated.Uint256(createdAt), 
                    new org.web3j.abi.datatypes.generated.Uint256(updatedAt));
            this.listingId = listingId;
            this.seller = seller;
            this.nftContract = nftContract;
            this.tokenId = tokenId;
            this.price = price;
            this.itemType = itemType;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Listing(Uint256 listingId, Address seller, Address nftContract, Uint256 tokenId, Uint256 price, Uint8 itemType, Uint8 status, Uint256 createdAt, Uint256 updatedAt) {
            super(listingId, seller, nftContract, tokenId, price, itemType, status, createdAt, updatedAt);
            this.listingId = listingId.getValue();
            this.seller = seller.getValue();
            this.nftContract = nftContract.getValue();
            this.tokenId = tokenId.getValue();
            this.price = price.getValue();
            this.itemType = itemType.getValue();
            this.status = status.getValue();
            this.createdAt = createdAt.getValue();
            this.updatedAt = updatedAt.getValue();
        }
    }

    public static class Transaction extends StaticStruct {
        public BigInteger listingId;

        public String buyer;

        public String seller;

        public BigInteger price;

        public BigInteger fee;

        public BigInteger timestamp;

        public Transaction(BigInteger listingId, String buyer, String seller, BigInteger price, BigInteger fee, BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.generated.Uint256(listingId), 
                    new org.web3j.abi.datatypes.Address(160, buyer), 
                    new org.web3j.abi.datatypes.Address(160, seller), 
                    new org.web3j.abi.datatypes.generated.Uint256(price), 
                    new org.web3j.abi.datatypes.generated.Uint256(fee), 
                    new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.listingId = listingId;
            this.buyer = buyer;
            this.seller = seller;
            this.price = price;
            this.fee = fee;
            this.timestamp = timestamp;
        }

        public Transaction(Uint256 listingId, Address buyer, Address seller, Uint256 price, Uint256 fee, Uint256 timestamp) {
            super(listingId, buyer, seller, price, fee, timestamp);
            this.listingId = listingId.getValue();
            this.buyer = buyer.getValue();
            this.seller = seller.getValue();
            this.price = price.getValue();
            this.fee = fee.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class FeeRateUpdatedEventResponse extends BaseEventResponse {
        public BigInteger oldRate;

        public BigInteger newRate;
    }

    public static class FeeRecipientUpdatedEventResponse extends BaseEventResponse {
        public String oldRecipient;

        public String newRecipient;
    }

    public static class ItemCancelledEventResponse extends BaseEventResponse {
        public BigInteger listingId;

        public String seller;
    }

    public static class ItemListedEventResponse extends BaseEventResponse {
        public BigInteger listingId;

        public String seller;

        public String nftContract;

        public BigInteger tokenId;

        public BigInteger price;

        public BigInteger itemType;
    }

    public static class ItemSoldEventResponse extends BaseEventResponse {
        public BigInteger listingId;

        public String buyer;

        public String seller;

        public BigInteger price;

        public BigInteger fee;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class PriceUpdatedEventResponse extends BaseEventResponse {
        public BigInteger listingId;

        public BigInteger oldPrice;

        public BigInteger newPrice;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }
}
