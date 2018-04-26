FROM openjdk:8u131-alpine

# Default values of environment variables
ENV version="v3.3.1"
ENV foldername="web3j-3.3.1"

ARG zipname="${foldername}".zip

RUN apk update \
    && apk add --no-cache bash \
    && apk add ca-certificates openssl unzip \
    && update-ca-certificates \
    && wget -O "${zipname}" https://github.com/web3j/web3j/releases/download/"${version}"/"${zipname}" \
    && unzip "${zipname}" \
    && cp /"${foldername}"/bin/web3j /usr/local/bin/ \
    && apk del ca-certificates openssl unzip \
    && rm -rf "${zipname}" \
    && rm -rf /var/cache/apk/*

ENTRYPOINT ["/web3j-3.3.1/bin/web3j"]

