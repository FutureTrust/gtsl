pragma solidity ^0.4.18;

import "./ConsortiumContract.sol";

contract RulesPropertiesContract {

  ConsortiumContract userOracle;
  bytes hash;

  event Updated(
    bytes hash,
    address user
  );

  function RulesPropertiesContract(address userOracleAddress) public {
    userOracle = ConsortiumContract(userOracleAddress);
  }

  function update(bytes _hash)
  public
  {
    require(userOracle.isAuthorized(msg.sender));
    hash = _hash;
    emit Updated(_hash, msg.sender);
  }

  function get()
  public
  view
  returns (bytes)
  {
    return hash;
  }

}
