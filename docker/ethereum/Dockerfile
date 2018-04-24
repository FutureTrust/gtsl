FROM ethereum/client-go:v1.6.5

# Default values of environment variables
ENV GEN_NONCE="0xeddeadbabeeddead" \
    DATA_DIR="/root/.ethereum" \
    CHAIN_TYPE="private" \
    NET_ID=1981 \
    ACCOUNT_NEW="false"

# Working directory
WORKDIR /opt

# Exposed ports
EXPOSE 30303
EXPOSE 8545

# Add entrypoint into container
ADD src/* /opt/
RUN chmod +x /opt/start_geth.sh

ENTRYPOINT /opt/start_geth.sh

