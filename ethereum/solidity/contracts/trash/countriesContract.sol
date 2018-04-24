pragma solidity ^0.4.16;


contract CountriesContract {

  enum ResponseCode { OK, NOT_FOUND, FORBIDDEN, BAD_REQUEST, CONFLICT }

  struct Country {
  bytes8 country;
  bytes8[] languages;
  }

  mapping (bytes8 => uint) public countryId;
  Country[] public countries;

  event CountryAdding(
  bytes8 country,
  bytes8[] languages,
  ResponseCode responseCode
  );

  event CountryUpdating(
  bytes8 country,
  bytes8[] languages,
  ResponseCode responseCode
  );

  event CountryRemoving(
  bytes8 country,
  ResponseCode responseCode
  );

  // Constructor

  function CountriesContract()
  public
  {
    // It’s necessary to add an empty first TSL, because a non-existing Country return 0
    countryId[""] = countries.length++;
  }

  function addCountry(bytes8 _country, bytes8[] _languages)
  public
  {
    ResponseCode responseCode = ResponseCode.OK;
    if (_country.length == 0  || _languages.length == 0 || containEmptyLanguage(_languages)) {
      responseCode = ResponseCode.BAD_REQUEST;
    } else if (exists(_country)) {
      responseCode = ResponseCode.CONFLICT;
    } else {
      if (responseCode == ResponseCode.OK) {
        countryId[_country] = countries.length;
        uint id = countries.length++;
        countries[id] = Country({country: _country, languages: _languages});
      }
    }
    CountryAdding(_country, _languages, responseCode);
  }

  function updateCountry(bytes8 _country, bytes8[] _languages)
  public
  {
    ResponseCode responseCode = ResponseCode.OK;
    if (_country.length == 0  || _languages.length == 0 || containEmptyLanguage(_languages)) {
      responseCode = ResponseCode.BAD_REQUEST;
    } else if (!exists(_country)) {
      responseCode = ResponseCode.NOT_FOUND;
    } else {
      uint id = countryId[_country];
      countries[id].languages = _languages;
    }
    CountryUpdating(_country, _languages, responseCode);
  }

  function removeCountry(bytes8 _country)
  public
  {
    ResponseCode responseCode = ResponseCode.OK;
    if (_country.length == 0) {
      responseCode = ResponseCode.BAD_REQUEST;
    } else if (!exists(_country)) {
      responseCode = ResponseCode.NOT_FOUND;
    } else {
      uint id = countryId[_country];
      for (uint i = id; i<countries.length-1; i++){
        countries[i] = countries[i+1];
      }
      delete countries[countries.length-1];
      countries.length--;
      delete countryId[_country];
    }
    CountryRemoving(_country, responseCode);
  }

  function existCountry(bytes8 _country)
  public
  constant
  returns (bool)
  {
    return countryId[_country] != 0;
  }

  function getLanguages(bytes8 _country)
  public
  constant
  returns (bytes8[])
  {
    return countries[countryId[_country]].languages;
  }

  function lengthCountries()
  public
  constant
  returns (uint)
  {
    return countries.length-1;
  }

  function exists(bytes8 _country)
  private
  constant
  returns (bool)
  {
    return countryId[_country] != 0;
  }

  function containEmptyLanguage(bytes8[] _languages)
  private
  constant
  returns (bool)
  {
    for (uint i = 0; i<_languages.length-1; i++){
      if (_languages[i].length == 0) {
        return true;
      }
    }
    return false;
  }

}
