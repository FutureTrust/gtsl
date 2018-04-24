pragma solidity ^0.4.11;

import "../utils/owned.sol";
import "./consortium.sol";

contract SimpleConsortium is Consortium, Owned {

    // Structures

    struct Member {
    uint since;
    bool authorized;
    }

    struct Proposal {
    address member;
    function (address) action;
    mapping (address => bool) voted;
    bool[] votes;
    bool result;
    bool executed;
    uint maxVoters;
    uint votingDeadline;
    uint date;
    }

    // Attributes

    mapping (address => Member) public members;
    Proposal[] proposals;
    uint public membersCount;
    uint public quorumInPercent;

    // Events

    event MembershipChanged(
    address member,
    string description,
    uint date
    );

    event ProposalAdded(
    uint proposalID,
    address member,
    uint votingDeadline,
    address creator,
    uint duration,
    uint date
    );

    event Voted(
    uint proposalID,
    address voter,
    uint date
    );

    event ProposalTallied(
    uint proposalID,
    bool result,
    uint votesFor,
    uint votesAgainst,
    uint nbOfVoters,
    uint maxVoters
    );

    // Modifiers

    modifier onlyMembers {
        require(isAuthorized(msg.sender));
        _;
    }

    // Constructor

    function SimpleConsortium(uint _quorumInPercent) public {
        addMember(owner);
        quorumInPercent = _quorumInPercent;
    }

    // Public functions

    function requestAddMember(address _targetMember, uint _durationInDays)
    public
    onlyMembers
    returns (uint proposalID)
    {
        require(!isAuthorized(_targetMember));
        return newProposal(_targetMember, addMember, _durationInDays);
    }

    function requestRemoveMember(address _targetMember, uint _durationInDays)
    public
    onlyMembers
    returns (uint proposalID)
    {
        require(isAuthorized(_targetMember));
        return newProposal(_targetMember, removeMember, _durationInDays);
    }

    function vote(uint _proposalID, bool _inSupport)
    public
    onlyMembers
    {
        require(!proposals[_proposalID].voted[msg.sender]
        && members[msg.sender].since < proposals[_proposalID].date
        && now < proposals[_proposalID].votingDeadline);
        proposals[_proposalID].voted[msg.sender] = true;
        proposals[_proposalID].votes.push(_inSupport);
        Voted(_proposalID, msg.sender, now);
    }

    function executeProposal(uint _proposalID)
    public
    onlyMembers
    returns (bool)
    {
        require(now >= proposals[_proposalID].votingDeadline
        && !proposals[_proposalID].executed);
        proposals[_proposalID].executed = true;

        uint votesFor = 0;
        uint votesAgainst = 0;
        uint quorum = (quorumInPercent * proposals[_proposalID].maxVoters);
        if (proposals[_proposalID].votes.length*100 < quorum) { // minimum quorum not reached
            proposals[_proposalID].result = false;
        } else {
            /* marjority algorithm */
            for (uint i = 0; i < proposals[_proposalID].votes.length; i++) {
                if (proposals[_proposalID].votes[i]) {
                    votesFor++;
                } else {
                    votesAgainst++;
                }
            }
            proposals[_proposalID].result = votesFor > votesAgainst;
        }
        /* log result */
        ProposalTallied(_proposalID, proposals[_proposalID].result, votesFor, votesAgainst,
        proposals[_proposalID].votes.length, proposals[_proposalID].maxVoters);
        /* execute result */
        if (proposals[_proposalID].result) {
            proposals[_proposalID].action(proposals[_proposalID].member);
        }
        return proposals[_proposalID].result;
    }

    // Private functions

    /* check if member is part of the consortium */
    function isAuthorized(address _member)
    private
    constant
    returns (bool)
    {
        return members[_member].authorized;
    }

    /* add a new member in the consortium */
    function addMember(address _targetMember)
    private
    {
        require(!isAuthorized(_targetMember));
        uint date = now;

        members[_targetMember].authorized = true;
        members[_targetMember].since = date;
        membersCount++;
        MembershipChanged(_targetMember, "added", date);
    }

    /* remove a member from the consortium */
    function removeMember(address _targetMember)
    private
    {
        require(isAuthorized(_targetMember));
        delete members[_targetMember];
        membersCount--;
        MembershipChanged(_targetMember, "removed", now);
    }

    /* add a new proposal which will be voted by the consortium */
    function newProposal(
    address _member,
    function(address) _action,
    uint _durationInDays
    )
    private
    returns (uint proposalID)
    {
        uint date = now;

        proposalID = proposals.length++;
        proposals[proposalID].member = _member;
        proposals[proposalID].action = _action;
        proposals[proposalID].maxVoters = membersCount;
        proposals[proposalID].votingDeadline = date + _durationInDays * 1 seconds;
        proposals[proposalID].date = date;

        ProposalAdded(proposalID, proposals[proposalID].member, proposals[proposalID].votingDeadline, msg.sender,
        _durationInDays, date);
    }

}