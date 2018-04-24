pragma solidity ^0.4.18;


contract ConsortiumContract {

  // Enums

  enum Action { ADD, REMOVE }

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
    Action action;
    mapping(address => bool) voted;
    uint votesFor;
    uint votesAgainst;
    bool result;
    bool executed;
    uint maxVoters;
    uint votingDeadline;
    uint date;
  }

  // Attributes
  mapping(address => Member) public members;
  address[] public memberAddresses;
  uint public quorumInPercent;
  Proposal[] proposals;

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
    require(isAuthorized());
    _;
  }

  modifier isMember(address _member) {
    require(isAuthorized(_member));
    _;
  }

  modifier isNotMember(address _member) {
    require(!isAuthorized(_member));
    _;
  }

  // Constructor

  function ConsortiumContract(uint _quorumInPercent, bytes32 _tslIdentifier) public {
    addMember(msg.sender, _tslIdentifier);
    quorumInPercent = _quorumInPercent;
  }

  // Public Functions

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
  returns (bool result, bool executed, address member, bytes32 tslIdentifier, Action action)
  {
    result = proposals[index].result;
    executed = proposals[index].executed;
    member = proposals[index].member;
    tslIdentifier = proposals[index].tslIdentifier;
    action = proposals[index].action;
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

  /* check if msg.sender can modify the tsl */
  function isSignerTsl(bytes32 _tslIdentifier)
  public
  view
  returns (bool)
  {
    return isSignerTsl(msg.sender, _tslIdentifier);
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

  /* create a proposal to add a new member */
  function requestAddMember(
    address _targetMember,
    bytes32 _tslIdentifier,
    uint _duration
  )
  public
  onlyMembers
  isNotMember(_targetMember)
  returns (uint)
  {
    return newProposal(_targetMember, _tslIdentifier, _duration, Action.ADD);
  }

  /* create a proposal to remove a new member */
  function requestRemoveMember(
    address _targetMember,
    uint _duration
  )
  public
  onlyMembers
  isMember(_targetMember)
  returns (uint)
  {
    return newProposal(_targetMember, members[_targetMember].tslIdentifier, _duration, Action.REMOVE);
  }

  /* allows a member to vote for or against a proposal */
  function vote(uint _proposalID, bool _inSupport)
  public
  onlyMembers
  {
    require(!proposals[_proposalID].voted[msg.sender]
    && members[msg.sender].since < proposals[_proposalID].date);

    if (now < proposals[_proposalID].votingDeadline) {
      // deadline not exceeded
      proposals[_proposalID].voted[msg.sender] = true;
      if (_inSupport) {
        proposals[_proposalID].votesFor++;
      }
      else {
        proposals[_proposalID].votesAgainst++;
      }
      emit Voted(_proposalID, msg.sender);
      tryToRunProposal(_proposalID);
    } else {
      // deadline exceeded
      runProposal(_proposalID);
    }
  }

  /* execute proposal */
  function executeProposal(uint _proposalID)
  public
  onlyMembers
  {
    require(now >= proposals[_proposalID].votingDeadline
      && !proposals[_proposalID].executed);
    runProposal(_proposalID);
  }

  // Private Functions

  /* compute the quorum for the proposal (in percent) */
  function computeQuorum(uint _proposalID)
  private
  view
  returns (uint)
  {
    return quorumInPercent * proposals[_proposalID].maxVoters;
  }

  /* compute the current vote couting */
  function computeTotalVotes(uint _proposalID)
  private
  view
  returns (uint)
  {
    return proposals[_proposalID].votesFor + proposals[_proposalID].votesAgainst;
  }

  /* true if the quorum has been reached */
  function quorumReached(uint _totalVotes, uint _quorum)
  private
  pure
  returns (bool)
  {
    return _totalVotes * 100 >= _quorum;
  }

  /* try to execute proposal */
  function tryToRunProposal(uint _proposalID)
  private
  {
    uint quorum = computeQuorum(_proposalID);
    uint totalVotes = computeTotalVotes(_proposalID);
    if (quorumReached(totalVotes, quorum)) {
      uint remainingVotes = proposals[_proposalID].maxVoters - totalVotes;
      if (proposals[_proposalID].votesAgainst >= remainingVotes + proposals[_proposalID].votesFor) {
        // votesAgainst are greater than or equals to votesFor + remaining votes
        // it means that even if all remaining voters vote for the proposal, the proposal will be rejected
        proposals[_proposalID].executed = true;
        emit ProposalTallied(_proposalID,proposals[_proposalID].result,proposals[_proposalID].votesFor,proposals[_proposalID].votesAgainst,totalVotes,proposals[_proposalID].maxVoters);
      } else if (proposals[_proposalID].votesFor > remainingVotes + proposals[_proposalID].votesAgainst) {
        // votesFor are greater than votesAgaint + remaining votes
        // it means that even if all remaining voters vote against the proposal, the proposal will be accepted
        doAction(_proposalID);
        proposals[_proposalID].result = true;
        proposals[_proposalID].executed = true;
        emit ProposalTallied(_proposalID,proposals[_proposalID].result,proposals[_proposalID].votesFor,proposals[_proposalID].votesAgainst,totalVotes,proposals[_proposalID].maxVoters);
      }
    }
  }

  /* execute proposal */
  function runProposal(uint _proposalID)
  private
  {
    uint quorum = computeQuorum(_proposalID);
    uint totalVotes = computeTotalVotes(_proposalID);
    if (quorumReached(totalVotes, quorum)) {
      proposals[_proposalID].result = proposals[_proposalID].votesFor > proposals[_proposalID].votesAgainst;
      if (proposals[_proposalID].result) {
        doAction(_proposalID);
      }
    }
    proposals[_proposalID].executed = true;
    emit ProposalTallied(
    _proposalID,
    proposals[_proposalID].result,
    proposals[_proposalID].votesFor,
  proposals[_proposalID].votesAgainst,
  totalVotes,
  proposals[_proposalID].maxVoters
    );
  }

  /* execute the action of the proposal */
  function doAction(uint _proposalID)
  private
  {
    if (proposals[_proposalID].action == Action.ADD) {
      addMember(proposals[_proposalID].member, proposals[_proposalID].tslIdentifier);
    } else if (proposals[_proposalID].action == Action.REMOVE) {
      removeMember(proposals[_proposalID].member, proposals[_proposalID].tslIdentifier);
    }
  }

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
    emit MembershipChanged(_targetMember, "added");
  }

  /* remove a member from the consortium */
  function removeMember(address _targetMember, bytes32 _tslIdentifier)
  private
  {
    require(isAuthorized(_targetMember) && isSignerTsl(_targetMember, _tslIdentifier));
    uint id = members[_targetMember].index;
    for (uint i = id; i < memberAddresses.length - 1; i++) {
      memberAddresses[i] = memberAddresses[i + 1];
      members[memberAddresses[i]].index = i;
    }
    delete memberAddresses[memberAddresses.length - 1];
    memberAddresses.length--;
    delete members[_targetMember];
    emit MembershipChanged(_targetMember, "removed");
  }

  /* create a new proposal which will be voted by the consortium */
  function newProposal(
    address _member,
    bytes32 _tslIdentifier,
    uint _duration,
    Action _action
  )
  private
  returns (uint proposalId)
  {
    uint date = now;
    uint deadline = date + _duration * 1 hours;
    Proposal memory proposal;
    proposal.member = _member;
    proposal.tslIdentifier = _tslIdentifier;
    proposal.action = _action;
    proposal.maxVoters = memberAddresses.length;
    proposal.votingDeadline = deadline;
    proposal.date = date;
    uint length = proposals.push(proposal);
    proposalId = length-1;
    emit ProposalAdded(proposalId, _member, deadline, msg.sender, _duration);
  }

}
