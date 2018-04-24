pragma solidity ^0.4.11;

contract Owned {
    address public owner;

    function Owned() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function transferOwnership(address _newOwner)
    onlyOwner
    public
    {
        owner = _newOwner;
    }
}