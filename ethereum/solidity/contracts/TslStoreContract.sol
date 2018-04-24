pragma solidity ^0.4.18;

import "./ConsortiumContract.sol";

contract TslStoreContract {

  // Structures

  struct Tsl {
    bytes32 code;
    bytes hash;
  }

  // Attributes

  ConsortiumContract userOracle;
  mapping (bytes32 => uint) private tslId;
  Tsl[] private gtsl;
  Tsl[] private repealed;

  // Events

  event TslAdding(
    bytes32 code,
    bytes hash,
    address user
  );

  event TslUpdating(
    bytes32 code,
    bytes hash,
    address user
  );

  event TslRemoving(
    bytes32 code,
    address user
  );

  // Modifiers

  modifier isSigner(bytes32 _code) {
    require(userOracle.isSignerTsl(msg.sender, _code));
    _;
  }

  modifier tslNotExists(bytes32 _code) {
    require(tslId[_code] == 0);
    _;
  }

  modifier tslExists(bytes32 _code) {
    require(tslId[_code] != 0);
    _;
  }

  // Constructor

  function TslStoreContract(address userOracleAddress) public {
    // It’s necessary to add an empty first TSL, because a non-existing TSL return 0
    tslId[""] = gtsl.length++;
    userOracle = ConsortiumContract(userOracleAddress); // known contract
  }

  // Public functions

  function addTsl(bytes32 _code, bytes _hash)
    public
    tslNotExists(_code)
    isSigner(_code)
  {
    require(_hash.length != 0);
    tslId[_code] = gtsl.length;
    gtsl.push(Tsl({code: _code, hash: _hash}));
    emit TslAdding(_code, _hash, msg.sender);
  }

  function updateTsl(bytes32 _code, bytes _hash)
    public
    tslExists(_code)
    isSigner(_code)
  {
    require(_hash.length != 0);
    gtsl[tslId[_code]].hash = _hash;
    emit TslUpdating(_code, _hash, msg.sender);
  }

  function removeTsl(bytes32 _code)
    public
    tslExists(_code)
    isSigner(_code)
  {
    uint id = tslId[_code];
    repealed.push(gtsl[id]);
    for (uint i = id; i<gtsl.length-1; i++){
      gtsl[i] = gtsl[i+1];
    }
    delete gtsl[gtsl.length-1];
    gtsl.length--;
    delete tslId[_code];
    emit TslRemoving(_code, msg.sender);
  }

  function getTsl(uint _index)
    public
    view
    returns (bytes32 code, bytes hash)
  {
    require(0 < _index && _index < gtsl.length);
    code = gtsl[_index].code;
    hash = gtsl[_index].hash;
  }

  function getTsl(bytes32 _code)
    public
    view
    returns (uint id, bytes hash)
  {
    id = tslId[_code];
    hash = gtsl[id].hash;
  }

  function exists(bytes32 _code)
  public
  view
  returns (bool)
  {
    return tslId[_code] != 0;
  }

  function length()
    public
    constant
    returns (uint)
  {
    return gtsl.length-1;
  }

}
