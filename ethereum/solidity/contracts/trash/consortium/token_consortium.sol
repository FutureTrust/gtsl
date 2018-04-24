pragma solidity ^0.4.11;

import "../utils/owned.sol";
import "./consortium.sol";

contract TokenConsortium is Consortium, Owned {

    function requestAddMember(address _targetMember, uint _durationInDays)
        public
        returns (uint proposalID)
    {
        return 1;
    }
    
    function requestRemoveMember(address _targetMember, uint _durationInDays)
        public
        returns (uint proposalID)
    {
        return 1;
    }
    
    function vote(uint _proposalID, bool _inSupport)
        public
    {
        
    }

    function executeProposal(uint _proposalID)
        public
        returns (bool result)
    {
        return false;    
    }

}