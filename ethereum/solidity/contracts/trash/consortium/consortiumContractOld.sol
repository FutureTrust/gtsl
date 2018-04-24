pragma solidity ^0.4.18;


contract ConsortiumContractOld {

  // Structures

  struct Member {
  uint index;
  uint since;
  bool authorized;
  bytes32 tslIdentifier;
  }

  struct Proposal {
  address member;
  bytes32 tslIdentifier;
  function (address, bytes32) action;
  bytes32 actionTitle;
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

  address[] public memberAddresses;

  Proposal[] proposals;

  uint public quorumInPercent;

  // Events

  event MembershipChanged(
  address member,
  string description
  );

  event ProposalAdded(
  uint proposalID,
  address member,
  uint votingDeadline,
  address creator,
  uint duration
  );

  event Voted(
  uint proposalID,
  address voter
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

  function ConsortiumContract(uint _quorumInPercent, bytes32 _tslIdentifier) public {
    addMember(msg.sender, _tslIdentifier);
    quorumInPercent = _quorumInPercent;
  }

  // Public functions

  function countMembers()
  public
  constant
  returns (uint)
  {
    return memberAddresses.length;
  }

  function countProposals()
  public
  constant
  returns (uint)
  {
    return proposals.length;
  }

  function getProposal(uint index)
  public
  view
  returns (bool result, bool executed, address member, bytes32 tslIdentifier, bytes32 actionTitle)
  {
    result = proposals[index].result;
    executed = proposals[index].executed;
    member = proposals[index].member;
    tslIdentifier = proposals[index].tslIdentifier;
    actionTitle = proposals[index].actionTitle;
  }

  /* check if member can modify the tsl */
  function authorizedTsl()
  public
  onlyMembers
  view
  returns (bytes32)
  {
    return members[msg.sender].tslIdentifier;
  }

  /* check if msg.sender is part of the consortium */
  function isAuthorized()
  public
  view
  returns (bool)
  {
    return isAuthorized(msg.sender);
  }

  /* check if member is part of the consortium */
  function isAuthorized(address _member)
  public
  view
  returns (bool)
  {
    return members[_member].authorized;
  }

  /* check if member can modify the tsl */
  function isSignerTsl(address _member, bytes32 _tslIdentifier)
  public
  view
  returns (bool)
  {
    require(_tslIdentifier != "");
    return members[_member].tslIdentifier == _tslIdentifier;
  }

  function requestAddMember(address _targetMember, bytes32 _tslIdentifier, uint _duration)
  public
  onlyMembers
  returns (uint proposalID)
  {
    require(!isAuthorized(_targetMember));
    proposalID = newProposal(_targetMember, _tslIdentifier, _duration);
    proposals[proposalID].action = addMember;
    proposals[proposalID].actionTitle = "add";
    return proposalID;
  }

  function requestRemoveMember(address _targetMember, bytes32 _tslIdentifier, uint _duration)
  public
  onlyMembers
  returns (uint proposalID)
  {
    require(isAuthorized(_targetMember) && isSignerTsl(_targetMember, _tslIdentifier));
    proposalID = newProposal(_targetMember, _tslIdentifier, _duration);
    proposals[proposalID].action = removeMember;
    proposals[proposalID].actionTitle = "remove";
  return proposalID;
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
    Voted(_proposalID, msg.sender);
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
    if (proposals[_proposalID].votes.length * 100 < quorum) {// minimum quorum not reached
      proposals[_proposalID].result = false;
    }
    else {
      /* marjority algorithm */
      for (uint i = 0; i < proposals[_proposalID].votes.length; i++) {
        if (proposals[_proposalID].votes[i]) {
          votesFor++;
        }
        else {
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
      proposals[_proposalID].action(proposals[_proposalID].member, proposals[_proposalID].tslIdentifier);
    }
    return proposals[_proposalID].result;
  }

  // Private functions

  /* add a new member in the consortium */
  function addMember(address _targetMember, bytes32 _tslIdentifier)
  private
  {
    require(!isAuthorized(_targetMember) && !isSignerTsl(_targetMember, _tslIdentifier));
    memberAddresses.push(_targetMember);
    members[_targetMember].index = memberAddresses.length - 1;
    members[_targetMember].authorized = true;
    members[_targetMember].since = now;
    members[_targetMember].tslIdentifier = _tslIdentifier;
    MembershipChanged(_targetMember, "added");
  }

  /* remove a member from the consortium */
  function removeMember(address _targetMember, bytes32 _tslIdentifier)
  private
  {
    require(isAuthorized(_targetMember) && isSignerTsl(_targetMember, _tslIdentifier));
    uint id = members[_targetMember].index;
    for (uint i = id; i < memberAddresses.length - 1; i++) {
      memberAddresses[i] = memberAddresses[i + 1];
    }
    delete memberAddresses[memberAddresses.length - 1];
    memberAddresses.length--;
    delete members[_targetMember];
    MembershipChanged(_targetMember, "removed");
  }

  /* add a new proposal which will be voted by the consortium */
  function newProposal(
  address _member,
  bytes32 _tslIdentifier,
  uint _duration
  )
  private
  returns (uint proposalID)
  {
    uint date = now;

    proposalID = proposals.length++;
    proposals[proposalID].member = _member;
    proposals[proposalID].tslIdentifier = _tslIdentifier;
    proposals[proposalID].maxVoters = memberAddresses.length;
    proposals[proposalID].votingDeadline = date + _duration * 1 hours;
    proposals[proposalID].date = date;

    ProposalAdded(proposalID, _member, proposals[proposalID].votingDeadline, msg.sender, _duration);
    return proposalID;
  }

}
