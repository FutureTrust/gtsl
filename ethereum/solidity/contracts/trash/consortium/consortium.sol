pragma solidity ^0.4.11;

contract Consortium {
    function requestAddMember(address _targetMember, uint _durationInDays)
    public
    returns (uint proposalID);

    function requestRemoveMember(address _targetMember, uint _durationInDays)
    public
    returns (uint proposalID);

    function vote(uint _proposalID, bool _inSupport)
    public;

    function executeProposal(uint _proposalID)
    public
    returns (bool result);
}